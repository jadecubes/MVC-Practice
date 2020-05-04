package books;

import jrails.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        //for BookView
        String[] titles = new String[]{ "Introduction of Algorithm","","Operating System",null,"Operational Research","Java Programming" };
        String [] authors=new String[]{"John Doe","Jack Smith","David Smith","Joe Smith",null,""};
        Book[] bList=new Book[6];
        //for Book model
        //Book controller
        for(int i=0;i<6;++i) {
            Book b = new Book();
            b.title = titles[i];
            b.author = authors[i];
            b.num_copies = 999;
            b.isThick=false;
            b.save(); // now the book is in the db
            b.num_copies = 42; // the book in the db still has 999 copies

            b.save(); // now the book in the db has 42 copies
            bList[i]=b;
        }
        bList[0].destroy();
        List<Book> bListFromDb = Model.all(Book.class);;
        bList[0].destroy();
        Model.reset();
    }
}