public class Main {
    public static void main(String[] args) {
        String giris = "(1+(7+(8*2-4))/23)*2/(1+(7+(8*2))/23)";
        String test1= "8+2*(7+(8*2))/23/2/8*2+10";
        String test2="(8*2)/23/2";
        String test3="(2*60+7/3)/7";
        String test4="7+(8*2-4)/3*2";

        IslemAgaci islemAgaci=new IslemAgaci(test4);
        islemAgaci.dallan();
        islemAgaci.solKokSag();
        System.out.println("islem sonucu = "+islemAgaci.isle());
    }
}
