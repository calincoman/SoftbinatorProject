package com.example.softbinatorproject.dtos;


import com.example.softbinatorproject.models.Bundle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreInfoDto {
    private String name;

    private String email;

    private String location;

    private String officeNumber;

    private List<Bundle> offeredBundles;
}
