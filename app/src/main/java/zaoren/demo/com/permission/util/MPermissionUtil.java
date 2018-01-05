package zaoren.demo.com.permission.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v7.view.menu.MenuWrapperFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import zaoren.demo.com.DebugLog;
import zaoren.demo.com.permission.annotation.OnMPermissionDenied;
import zaoren.demo.com.permission.annotation.OnMPermissionGranted;
import zaoren.demo.com.permission.annotation.OnMPermissionNeverAskAgain;

/**
 * Created by Administrator on 2018/1/4.
 */
final public class MPermissionUtil {
    public static boolean isOverMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static Activity getActivity(Object object) {
        if (object instanceof Fragment) {
            return ((Fragment) object).getActivity();
        } else if (object instanceof Activity) {
            return (Activity) object;
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static List<String> findDeniedPermissions(Activity activity, String... permissions) {
        List<String> denyPermissions = new ArrayList<>();
        for (String value : permissions) {
            if (activity.checkSelfPermission(value) != PackageManager.PERMISSION_GRANTED) {
                denyPermissions.add(value);
            }
        }
        return denyPermissions;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public  static  List<String> findNeverAskAgainPermissions(Activity activity, String... permissions){
        List<String> neverAskAgainPermission=new ArrayList<>();
        for (String value :
                permissions) {
            if (activity.checkSelfPermission(value)!=PackageManager.PERMISSION_GRANTED&&!activity.shouldShowRequestPermissionRationale(value)){
                neverAskAgainPermission.add(value);
            }
        }
        return neverAskAgainPermission;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static  List<String> findDeniedPermissionWithoutNeverAskAgain(Activity activity, String... permissions){
        List<String> denyPermissions=new ArrayList<>();
        for (String value:
             permissions) {
            if (activity.checkSelfPermission(value)!=PackageManager.PERMISSION_GRANTED&&activity.shouldShowRequestPermissionRationale(value)){
                denyPermissions.add(value);
            }
        }
        return denyPermissions;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static boolean hasNeverAskAgainPermission(Activity activity, List<String> permission) {
        for (String value :
                permission) {
            if (activity.checkSelfPermission(value) != PackageManager.PERMISSION_GRANTED && !activity.shouldShowRequestPermissionRationale(value)) {
                return true;
            }

        }
        return false;
    }

    public static  <A extends Annotation>Method findMethodWithRequestCode(Class clazz,Class<A> annotation,int requestCode){
        for (Method method:clazz.getDeclaredMethods()){
            if (method.isAnnotationPresent(annotation)){
                if (isEqualRequestCodeFromAnnotation(method,annotation,requestCode)){
                    return method;
                }
            }
        }
        return null;
    }

    private   static  boolean isEqualRequestCodeFromAnnotation(Method method,Class clazz,int requestCode){
        if (clazz.equals(OnMPermissionDenied.class)){
            return  requestCode==method.getAnnotation(OnMPermissionDenied.class).value();
        }else  if (clazz.equals(OnMPermissionGranted.class)){
            return  requestCode==method.getAnnotation(OnMPermissionGranted.class).value();
        }else if (clazz.equals(OnMPermissionNeverAskAgain.class)){
            return  requestCode==method.getAnnotation(OnMPermissionNeverAskAgain.class).value();
        }else {
            return  false;
        }
    }

    public static String toString(List<String> permission) {
        if (permission==null||permission.isEmpty()){
            return "";
        }
        return toString(permission.toArray(new String[permission.size()]));
    }

    private   static String toString(String[] permission){
        if (permission==null||permission.length<=0)
            return "";
        StringBuilder stringBuilder = new StringBuilder();
        for (String p :
                permission) {
            stringBuilder.append(p.replaceFirst("android.permission",""));
            stringBuilder.append(",");
        }

        stringBuilder.deleteCharAt(stringBuilder.length()-1);
        return  stringBuilder.toString();
    }
}
