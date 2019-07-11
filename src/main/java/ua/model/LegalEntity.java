package ua.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@SuppressWarnings("unused")
@Entity
@Table(name = "entity")
public class LegalEntity {

    public static final String TYPE_ENTITY_HOMEOWNERS = "homeowners";
    public static final String TYPE_ENTITY_COMPANY = "company";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Size(max = 20)
    @Column(name = "subdomain", unique = true)
    private String subdomain;

    @NotNull
    @Size(max = 50)
    @Column(name = "name")
    private String name;

    @NotNull
    @Size(max = 20)
    @Column(name = "type")
    private String type;

    @Size(max = 1024)
    @Column(name = "about")
    private String about;

    @Size(max = 1024)
    @Column(name = "contact_info")
    private String contactInfo;

    @Size(max = 20)
    @Column(name = "theme")
    private String theme;

    @OneToOne
    @JoinColumn(name = "home_default_id")
    private Home defaultHome;

    @JsonManagedReference
    @Fetch(value = FetchMode.SUBSELECT)
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "legalEntity")
    private List<Home> homes;

    @JsonManagedReference
    @Fetch(value = FetchMode.SUBSELECT)
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "legalEntity")
    private List<News> news;

    @OneToMany(mappedBy = "legalEntity")
    private List<Service> services;

    @OneToMany(mappedBy = "legalEntity")
    private List<Bank> banks;

    public int getId() {
        return id;
    }

    public static LegalEntity getCurrentLegalEntity(){
        return null;
    }

    public String getSubdomain() {
        return subdomain;
    }

    public void setSubdomain(String subdomain) {
        this.subdomain = subdomain;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public Home getDefaultHome() {
        return defaultHome;
    }

    public void setDefaultHome(Home defaultHome) {
        this.defaultHome = defaultHome;
    }

    public List<Bank> getBanks() {
        return banks;
    }

    public List<Home> getHomes() {
        return homes;
    }

    public List<News> getNews() {
        return news;
    }

    public List<Service> getServices() {
        return services;
    }
}
