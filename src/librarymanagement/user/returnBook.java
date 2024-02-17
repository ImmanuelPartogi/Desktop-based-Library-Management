
package librarymanagement.user;

import java.sql.Date;

public class returnBook {
    
    private final String title;
    private final String author;
    private final String genre;
    private final Date date;
    private final String image;
    
    public returnBook(String title, String author, String genre, String image, Date date){
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.image = image;
        this.date = date;
    }
    
    public String getTitle(){
        return title;
    }
    public String getAuthor(){
        return author;
    }
    public String getGenre(){
        return genre;
    }
    public String getImage(){
        return image;
    }
    public Date getDate(){
        return date;
    }
    
}
