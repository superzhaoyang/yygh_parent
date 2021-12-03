import com.superzhaoyang.yygh.common.utils.MD5;

public class ServiceHospApplication {

    public static void main(String[] args) {
        String str = MD5.encrypt("1234567890");
        System.out.println(str);
    }
}
