package com.ormi.mogakcote.problem.application;

import com.ormi.mogakcote.common.dto.SuccessResponse;
import com.ormi.mogakcote.exception.dto.ErrorType;
import com.ormi.mogakcote.exception.problem.LanguageInvalidException;
import com.ormi.mogakcote.problem.domain.Language;
import com.ormi.mogakcote.problem.dto.request.LanguageRequest;
import com.ormi.mogakcote.problem.dto.response.LanguageResponse;
import com.ormi.mogakcote.problem.infrastructure.LanguageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LanguageService {

    private final LanguageRepository languageRepository;

    @Transactional
    public LanguageResponse createLanguage(
            LanguageRequest request
    ) {
        Language language = buildLanguage(request);
        Language savedLanguage = languageRepository.save(language);

        return LanguageResponse.toResponse(
                savedLanguage.getId(),
                savedLanguage.getLanguageName()
        );
    }

    @Transactional
    public LanguageResponse updateLanguage(Long id, LanguageRequest request) {
        Language findLanguage = getLanguageOrThrowIfNotExist(id);

        findLanguage.update(request.getLanguageName());

        return LanguageResponse.toResponse(
                findLanguage.getId(),
                findLanguage.getLanguageName()
        );
    }

    @Transactional
    public SuccessResponse deleteLanguage(Long id) {
        Language findLanguage = getLanguageOrThrowIfNotExist(id);
        languageRepository.deleteById(findLanguage.getId());

        return new SuccessResponse("작성언어 삭제를 성공했습니다.");
    }

    @Transactional
    public List getLanguageList() {
        List<LanguageResponse> languageResponses = new ArrayList<>();
        List<Language> findLanguages = languageRepository.findAll();

        findLanguages.forEach(findLanguage -> {
            languageResponses.add(LanguageResponse.toResponse(
                    findLanguage.getId(),
                    findLanguage.getLanguageName()
            ));
        });

        return languageResponses;
    }


    private Language buildLanguage(LanguageRequest request) {
        return Language.builder()
                .languageName(request.getLanguageName())
                .build();
    }

    private Language getLanguageOrThrowIfNotExist(Long languageId) {
        return languageRepository.findById(languageId).orElseThrow(
                () -> new LanguageInvalidException(ErrorType.LANGUAGE_NOT_FOUND_ERROR)
        );
    }

//    private void throwsIfLanguageNotExist(Long languageId) {
//        languageRepository.findByLanguageId(languageId).orElseThrow(
//                () -> new AlgorithmInvalidException(ErrorType.LANGUAGE_NOT_FOUND_ERROR));
//    }
}