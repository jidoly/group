package jidoly.group.com;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class CustomAuditorAware implements AuditorAware<Long> {
    @Override
    public Optional<Long> getCurrentAuditor() {
        // SecurityContextHolder를 사용하여 현재 사용자 정보를 가져온다
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof CustomUser) {
                CustomUser customUser = (CustomUser) principal;
                Long userId = customUser.getId();
                // 이제 userId를 사용할 수 있다.
                return Optional.of(userId);
            }
        }
        return Optional.empty();
    }
}
