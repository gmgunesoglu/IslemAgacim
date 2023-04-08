import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IslemAgaci {
    private Dugum kok;
    private boolean sonucTanimli=true;
    private String islemGirdisi;

    private Terim terim;

    public IslemAgaci(String islemGirdisi) {
        this.islemGirdisi = islemGirdisi;
        kok=new Dugum(islemGirdisi);
    }

    public double isle(){
        if (kok!=null){
            return hesapla(kok);
        }
        System.out.printf("Kok Yok!");
        return 0;
    }

    private double hesapla(Dugum dugum){
        if(dugum.solDugum==null){
            if(dugum.sagDugum==null){
                return dugum.deger;
            }else{
                return islem(dugum.deger,dugum.islem,hesapla(dugum.sagDugum));
            }
        }else{
            return islem(hesapla(dugum.solDugum),dugum.islem,hesapla(dugum.sagDugum));
        }
    }

    private double islem(double sayi1,char islem,double sayi2){
        if(islem=='+'){
            return sayi1+sayi2;
        }if(islem=='-'){
            return sayi1-sayi2;
        }if(islem=='*'){
            return sayi1*sayi2;
        }if(sayi2==0){
            System.out.printf("Tanimsiz");
            sonucTanimli=false;
            return 0;
        }
        return sayi1/sayi2;
    }

    public void dallan(){
//        System.out.println("kök terim:\t"+islemGirdisi);
        dallan(kok);
    }

    private void dallan(Dugum dugum){
        Terim terim=terimleriGetir(dugum.terim);
        if(terim!=null){
            dugum.islem=terim.islem;
//            System.out.println("["+dugum.terim+"] sola >> ["+terim.ilkTerim+"]");
            dugum.solDugum=new Dugum(terim.ilkTerim);
            dallan(dugum.solDugum);
//            System.out.println("["+dugum.terim+"] saga >> ["+terim.kalanTerim+"]");
            dugum.sagDugum=new Dugum(terim.kalanTerim);
            dallan(dugum.sagDugum);
        }else{
//            System.out.println("yaprak degeri: "+dugum.terim);
            dugum.deger=Double.parseDouble(dugum.terim);
        }
    }

    public void solKokSag(){
        if(kok==null){
            System.out.println("Kok Yok!");
            return;
        }
        System.out.println("kök terim:\t"+islemGirdisi);
        solKokSag(kok);
    }

    private void solKokSag(Dugum dugum){
        if(dugum.solDugum!=null){
            System.out.println("Sola indim.");
            solKokSag(dugum.solDugum);
        }

        if(dugum.solDugum==null&&dugum.sagDugum==null){
            System.out.println("Yaprak Degeri: "+dugum.terim);
        }else{
            System.out.println("Dugum islemi:  "+dugum.islem);
        }
        if(dugum.sagDugum!=null){
            System.out.println("Saga indim.");
            solKokSag(dugum.sagDugum);
        }
        System.out.println("Yukari ciktim.");
    }

    public Terim terimleriGetir(String girdi){
        Matcher matcher=Pattern.compile("[\\/*+-]").matcher(girdi);
        if(!matcher.find()){
            return null;
            //null dondugunde nerede çağırıldıysa orası yaprak olmalı...
        }
        Terim terim=new Terim();
        for(int i=0;i<girdi.length();i++){
            if(girdi.charAt(i)=='+'||girdi.charAt(i)=='-'){
                terim.ilkTerim=girdi.substring(0,i);
                terim.islem=girdi.charAt(i);
                //eğer buradaysak i+1 < girdi.lenght !
                terim.kalanTerim=girdi.substring(i+1);
                return terim;
            }else if(girdi.charAt(i)=='('){
                //parantezli kısımların içine girilmeden önce + - operatörleri kontrol ediliyor
                //tüm terim parantez içerisindeyse ...
                if(i==0){
                    i=parantezKapananaKadarIlerle(i,girdi);
                    if(i==girdi.length()-1){
                        return terimleriGetir(girdi.substring(1,girdi.length()-1));
                    }else{
                        i=0;
                    }
                }
                i=parantezKapananaKadarIlerle(i,girdi);
            }
        }
        //buraya geldiyse hiç + - operatörü yok. o zaman... Yani tek terim...
        //tek terim ve parantezle baslayıp bitmiyor eğer öyle ise bile farklı parantezler!
        return terimiParcala(girdi);
    }

    private int parantezKapananaKadarIlerle(int baslangic,String girdi){
        int stack=1;
        while(stack!=0){
            baslangic++;
            if(girdi.charAt(baslangic)=='('){
                stack++;
            }else if(girdi.charAt(baslangic)==')'){
                stack--;
            }
        }
        return baslangic;
    }

    private Terim terimiParcala(String girdi){
        //tek terim, içerisinde parantezli ifade olabilir, parantezli ifadenin içerisinde bölme olabilir !
        Terim terim=new Terim();
        //ilk baş bölme kontrol edilmeli
        for(int i=0;i<girdi.length();i++){
            //parantez içine karşımıyoruz!!!
            if(girdi.charAt(i)=='('){
                i=parantezKapananaKadarIlerle(i,girdi);
            }
            else if(girdi.charAt(i)=='/'){
                int baslangic=i+1;
                int bitis=i+1;
                terim.islem='/';
                //bölüm parantezli ifade ile yapılıyorsa
                if(girdi.charAt(i+1)=='('){
                    bitis=parantezKapananaKadarIlerle(baslangic,girdi)+1;
                    terim.kalanTerim=girdi.substring(baslangic,bitis);
                    terim.ilkTerim=girdi.substring(0,i);
                    if(bitis!=girdi.length()){
                        terim.ilkTerim+=girdi.substring(bitis,girdi.length());
                    }
                    return terim;
                }
                //bölüm bir sayı ile yapılıyorsa
                else{
                    while(bitis<girdi.length()){
                        if(girdi.charAt(bitis)>=48&&girdi.charAt(bitis)<=57){
                            bitis++;
                        }else{
                            break;
                        }
                    }
                    terim.kalanTerim=girdi.substring(baslangic,bitis);
                    terim.ilkTerim=girdi.substring(0,i);
                    if(bitis!=girdi.length()){
                        terim.ilkTerim+=girdi.substring(bitis,girdi.length());
                    }
                    return terim;
                }
            }
        }
        //buraya kadar geldiysek / yok sadece * var ama parantezlere dikkat !
        //ama buraya kadar geldiysek parantez * dan önce ise direk baştadır.
        if(girdi.charAt(0)=='('){
            int bitis=parantezKapananaKadarIlerle(0,girdi);
            terim.islem='*';
            terim.ilkTerim=girdi.substring(0,bitis+1);
            terim.kalanTerim=girdi.substring(bitis+2);
            return terim;
        }else{
            int bitis=girdi.indexOf('*');
            terim.islem='*';
            terim.ilkTerim=girdi.substring(0,bitis);
            terim.kalanTerim=girdi.substring(bitis+1,girdi.length());
            return terim;
        }
    }

}
