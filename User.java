public class User {
    private String name;
    private String locality;
    private String age;

    public User(String name, String locality, String age) {
        this.name = name;
        this.locality = locality;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" + "name=" + name + ", locality=" + locality + ", age=" + age + '}';
    }
}