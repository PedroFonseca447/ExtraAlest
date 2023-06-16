public class App {
    

    public static void main(String args[]){
        Avl a = new Avl();



        a.addRoot(10);
        a.add(12);
        a.add(2);
        a.add(4);
        a.add(5);
        a.add(3);
        a.add(1);
    
        System.out.println(a.contains(2));
     
        a.generateDot("exemplo");
    }
}
