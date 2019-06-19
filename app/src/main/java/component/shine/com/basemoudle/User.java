package component.shine.com.basemoudle;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;



/**
 * Created by cc
 * On 2019/6/19.
 */


@Entity
public class User {
    @Id(autoincrement = true)
    private Long id;

    @NotNull
    private String name;
    private String sex;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String age;
    private String email;

    @Generated(hash = 1028900582)
    public User(Long id, @NotNull String name, String sex, String age,
            String email) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.email = email;
    }

    @Generated(hash = 586692638)
    public User() {
    }

}
