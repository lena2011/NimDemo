package zaoren.demo.com.base.util;

import java.util.UUID;

/**
 * Created by Administrator on 2018/1/5.
 */

public class StringUtil {

    public  static String  get36UUID(){
        UUID uuid=UUID.randomUUID();
        String uniqueId=uuid.toString();
        return uniqueId;
    }

}
