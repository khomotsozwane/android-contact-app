package blubnanacom.androidcontactappassisgnment;

public class ContactData {
    private int Id;
    private String Name;
    private String Email;
    private String Phone;
    private int Image;
    private String Color;

    public ContactData(){}
    public ContactData(int id, String name, String email, String phone, String color, int image){
        this.Id = id;
        this.Name = name;
        this.Email = email;
        this.Phone = phone;
        this.Image = image;
        this.Color = color;
    }

    public ContactData(int id, String name, String email, String phone){
        this.Id = id;
        this.Name = name;
        this.Email = email;
        this.Phone = phone;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }
}
