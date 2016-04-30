package com.yxkang.android.alipay.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;

/**
 * Created by fine on 2016/4/28.
 */
public class SignaturesUtil {

    private static final String TAG = "SignaturesUtil";

    /**
     * 获取未安装的APK的签名信息
     *
     * @param apkPath apk路径
     * @return APK的签名信息
     */
    @SuppressWarnings("unchecked")
    public static String getNotInstalledAPKSignatures(String apkPath) {
        String PATH_PackageParser = "android.content.pm.PackageParser";
        try {
            // apk包的文件路径
            // 这是一个Package 解释器, 是隐藏的
            // 构造函数的参数只有一个, apk文件的路径
            // PackageParser packageParser = new PackageParser(apkPath);
            Class pkgParserCls = Class.forName(PATH_PackageParser);
            Class[] typeArgs = new Class[1];
            typeArgs[0] = String.class;
            Constructor pkgParserCt = pkgParserCls.getConstructor(typeArgs);
            Object[] valueArgs = new Object[1];
            valueArgs[0] = apkPath;
            Object pkgParser = pkgParserCt.newInstance(valueArgs);
            Log.i(TAG, "showUninstallAPKSignatures: pkgParser = " + pkgParser.toString());
            // 这个是与显示有关的, 里面涉及到一些像素显示等等, 我们使用默认的情况
            DisplayMetrics metrics = new DisplayMetrics();
            metrics.setToDefaults();
            // PackageParser.Package mPkgInfo = packageParser.parsePackage(new
            // File(apkPath), apkPath,
            // metrics, 0);
            typeArgs = new Class[4];
            typeArgs[0] = File.class;
            typeArgs[1] = String.class;
            typeArgs[2] = DisplayMetrics.class;
            typeArgs[3] = Integer.TYPE;
            Method pkgParser_parsePackageMtd = pkgParserCls.getDeclaredMethod("parsePackage",
                    typeArgs);
            valueArgs = new Object[4];
            valueArgs[0] = new File(apkPath);
            valueArgs[1] = apkPath;
            valueArgs[2] = metrics;
            valueArgs[3] = PackageManager.GET_SIGNATURES;
            Object pkgParserPkg = pkgParser_parsePackageMtd.invoke(pkgParser, valueArgs);

            typeArgs = new Class[2];
            typeArgs[0] = pkgParserPkg.getClass();
            typeArgs[1] = Integer.TYPE;
            Method pkgParser_collectCertificatesMtd = pkgParserCls.getDeclaredMethod("collectCertificates",
                    typeArgs);
            valueArgs = new Object[2];
            valueArgs[0] = pkgParserPkg;
            valueArgs[1] = PackageManager.GET_SIGNATURES;
            pkgParser_collectCertificatesMtd.invoke(pkgParser, valueArgs);
            // 应用程序信息包, 这个公开的, 不过有些函数, 变量没公开
            Field packageInfoFld = pkgParserPkg.getClass().getDeclaredField("mSignatures");
            Signature[] info = (Signature[]) packageInfoFld.get(pkgParserPkg);
            Log.i(TAG, "showUninstallAPKSignatures: size = " + info.length);
            Log.i(TAG, "showUninstallAPKSignatures: " + info[0].toCharsString());
            return info[0].toCharsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取app自身的签名
     *
     * @param context 上下文
     * @return app自身的签名
     */
    public static String getSelfSignatures(Context context) {
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> apps = pm.getInstalledPackages(PackageManager.GET_SIGNATURES);
        for (PackageInfo packageinfo : apps) {
            String packageName = packageinfo.packageName;
            if (packageName.equals(context.getPackageName())) {
                Signature[] signatures = packageinfo.signatures;
                if (signatures == null || signatures.length == 0) {
                    return null;
                }
                Log.i(TAG, "getSelfSignatures: length = " + signatures.length);
                StringBuilder buffer = new StringBuilder();
                for (Signature signature : signatures) {
                    buffer.append(MD5.getMessageDigest(signature.toByteArray()));
                }
                Log.i(TAG, "getSelfSignatures: " + buffer.toString());
                parseSignature(signatures[0].toByteArray());
                return buffer.toString();
            }
        }
        return null;
    }

    /**
     * 获取已安装的app的签名信息
     *
     * @param context 上下文
     */
    @SuppressLint("PackageManagerGetSignatures")
    public static void getInstalledAPKSignatures(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo("com.yxkang.android.alipay",
                    PackageManager.GET_SIGNATURES);
            Signature[] signs = packageInfo.signatures;
            if (signs == null || signs.length == 0) {
                return;
            }
            StringBuilder buffer = new StringBuilder();
            for (Signature signature : signs) {
                buffer.append(MD5.getMessageDigest(signature.toByteArray()));
            }
            Log.i(TAG, "getInstalledAPKSignatures: " + buffer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void parseSignature(byte[] signature) {
        try {
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) certFactory.generateCertificate(new ByteArrayInputStream(signature));
            String pubKey = cert.getPublicKey().toString();
            String signNumber = cert.getSerialNumber().toString();
            Log.i(TAG, "parseSignature: signName = " + cert.getSigAlgName());
            Log.i(TAG, "parseSignature: pubKey = " + pubKey);
            Log.i(TAG, "parseSignature: signNumber = " + signNumber);
            Log.i(TAG, "parseSignature: subjectDN = "+cert.getSubjectDN().toString());
        } catch (CertificateException e) {
            e.printStackTrace();
        }
    }
}
