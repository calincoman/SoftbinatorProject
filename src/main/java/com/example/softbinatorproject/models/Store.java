package com.example.softbinatorproject.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "stores")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String password;

    private String location;

    private String officeNumber;

    private String identificationCode;

    @OneToMany
    private List<Bundle> offeredBundles;

    public void addBundle(Bundle bundle) {
        offeredBundles.add(bundle);
    }

    public void deleteBundle(Bundle bundle) {
        offeredBundles.remove(bundle);
    }
}
