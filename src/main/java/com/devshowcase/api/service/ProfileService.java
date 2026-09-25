package com.devshowcase.api.service;

import com.devshowcase.api.dtos.ProfileRequestDTO;
import com.devshowcase.api.dtos.ProfileResponseDTO;
import com.devshowcase.api.model.Profile;
import com.devshowcase.api.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Transactional
    public ProfileResponseDTO createProfile(ProfileRequestDTO dto) {
        Profile profile = new Profile();
        profile.setName(dto.name());
        profile.setEmail(dto.email());
        profile.setBio(dto.bio());

        Profile saved = profileRepository.save(profile);
        return new ProfileResponseDTO(saved.getId(), saved.getName(), saved.getEmail(), saved.getBio());
    }

    @Transactional(readOnly = true)
    public ProfileResponseDTO getProfile(Long id) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Perfil não encontrado"));
        return new ProfileResponseDTO(profile.getId(), profile.getName(), profile.getEmail(), profile.getBio());
    }
}