package zaoren.demo.com.permission;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.Fragment;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import zaoren.demo.com.permission.annotation.OnMPermissionDenied;
import zaoren.demo.com.permission.annotation.OnMPermissionGranted;
import zaoren.demo.com.permission.annotation.OnMPermissionNeverAskAgain;
import zaoren.demo.com.permission.util.MPermissionUtil;

import static zaoren.demo.com.permission.util.MPermissionUtil.getActivity;

/**
 * Created by Administrator on 2018/1/4.
 */

public class MPermission {
    private String[] permissions;
    private int requestCode;
    private Object mObject;//activity or  fragment


    private MPermission(Object object) {
        mObject = object;
    }

    public static MPermission with(Activity activity) {
        return new MPermission(activity);
    }

    public static MPermission with(Fragment fragment) {
        return new MPermission(fragment);
    }

    public MPermission permissions(String... permissions) {
        this.permissions = permissions;
        return this;
    }

    public MPermission addRequestCode(int requestCode) {
        this.requestCode = requestCode;
        return this;
    }

    public static List<String> getDeniedPermissions(Activity activity, String[] permissions) {
        return getDeniedPermissions((Object) activity, permissions);
    }

    public static List<String> getDeniedPermissions(Fragment fragment, String[] permissions) {
        return getDeniedPermissions((Object) fragment, permissions);
    }

    public static List<String> getNeverAskAgainPermission(Activity activity, String[] permissions) {
        return getNeverAskAgainPermission((Object) activity, permissions);
    }

    public static List<String> getNeverAskAgainPermission(Fragment fragment, String[] permissions) {
        return getNeverAskAgainPermission((Object) fragment, permissions);
    }

    public static  List<String> getDeniedPermissionsWithoutNeverAskAgain(Activity activity,String[] permissions){
        return getDeniedPermissionsWithoutNeverAskAgain((Object)activity,permissions);
    }

    public static  List<String> getDeniedPermissionsWithoutNeverAskAgain(Fragment fragment,String[] permissions){
        return getDeniedPermissionsWithoutNeverAskAgain((Object)fragment,permissions);
    }

    private static List<String> getDeniedPermissions(Object object, String[] permissions) {
        if (permissions == null || permissions.length <= 0) {
            return null;
        }
        return MPermissionUtil.findDeniedPermissions(getActivity(object), permissions);
    }

    private static List<String> getNeverAskAgainPermission(Object object, String[] permissions) {
        if (permissions == null || permissions.length <= 0)
            return null;
        return MPermissionUtil.findNeverAskAgainPermissions(getActivity(object), permissions);

    }

    private  static  List<String> getDeniedPermissionsWithoutNeverAskAgain(Object object,String[] permissions){
        if (permissions==null||permissions.length<=0){
            return  null;
        }
        return MPermissionUtil.findDeniedPermissionWithoutNeverAskAgain(getActivity(object),permissions);
    }

    @TargetApi(value = Build.VERSION_CODES.M)
    public void request() {
        requestPermissions(mObject, requestCode, permissions);
    }

    @TargetApi(value = Build.VERSION_CODES.M)
    private static void requestPermissions(Object object, int requestCode, String[] permissions) {
        if (!MPermissionUtil.isOverMarshmallow()) {
            doExecuteSuccess(object, requestCode);
            return;
        }
        List<String> deniedPermissions = MPermissionUtil.findDeniedPermissions(getActivity(object), permissions);

        if (deniedPermissions.size() > 0) {
            if (object instanceof Activity) {
                ((Activity) object).requestPermissions(deniedPermissions.toArray(new String[deniedPermissions.size()]), requestCode);
            } else if (object instanceof Fragment) {
                ((Fragment) object).requestPermissions(deniedPermissions.toArray(new String[deniedPermissions.size()]), requestCode);
            } else {
                throw new IllegalArgumentException(object.getClass().getName() + "is not supported");
            }
        } else {
            doExecuteSuccess(object, requestCode);
        }

    }

    public static void onRequestPermissionsResult(Activity activity, int requestCode, String[] permissions, int[] grantResults) {
        requestResult(activity, requestCode, permissions, grantResults);
    }

    private static void requestResult(Object object, int requestCode, String[] permissions, int[] grantResults) {
        List<String> deniedPermissions = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(permissions[i]);
            }
        }

        if (deniedPermissions.size() > 0) {
            if (MPermissionUtil.hasNeverAskAgainPermission(getActivity(object), deniedPermissions)) {
                doExecuteFailAsNeverAskAgain(object, requestCode);
            } else {
                doExecuteFail(object, requestCode);
            }
        } else {
            doExecuteSuccess(object, requestCode);
        }
    }

    private static void doExecuteSuccess(Object object, int requestCode) {
        executeMethod(object, MPermissionUtil.findMethodWithRequestCode(object.getClass(), OnMPermissionGranted.class, requestCode));
    }

    private static void doExecuteFail(Object object, int requestCode) {
        executeMethod(object, MPermissionUtil.findMethodWithRequestCode(object.getClass(), OnMPermissionDenied.class, requestCode));
    }

    private static void doExecuteFailAsNeverAskAgain(Object object, int requestCode) {
        executeMethod(object, MPermissionUtil.findMethodWithRequestCode(object.getClass(), OnMPermissionNeverAskAgain.class, requestCode));
    }


    private static void executeMethod(Object object, Method executeMethod) {
        executeMethodWithParam(object, executeMethod, new Object[]{});
    }

    private static void executeMethodWithParam(Object object, Method executeMethod, Object... args) {
        if (executeMethod != null) {
            try {
                if (!executeMethod.isAccessible()) {
                    executeMethod.setAccessible(true);
                }
                executeMethod.invoke(object, args);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

}

