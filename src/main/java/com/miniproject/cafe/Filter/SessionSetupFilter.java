package com.miniproject.cafe.Filter;

import com.miniproject.cafe.Mapper.AdminMapper;
import com.miniproject.cafe.Mapper.MemberMapper;
import com.miniproject.cafe.VO.AdminVO;
import com.miniproject.cafe.VO.MemberVO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class SessionSetupFilter extends OncePerRequestFilter {

    private final MemberMapper memberMapper;
    private final AdminMapper adminMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {

            HttpSession session = request.getSession(true);
            Object principal = auth.getPrincipal();
            String loginId = auth.getName();    // principal이 문자열일 때 사용됨

            // 1) 관리자일 경우
            boolean isAdmin = auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            if (isAdmin) {
                if (session.getAttribute("admin") == null) {

                    AdminVO admin;

                    if (principal instanceof AdminVO) {
                        admin = (AdminVO) principal;
                    } else {
                        // Remember-Me 상황 → DB에서 가져오기
                        admin = adminMapper.findById(loginId);
                    }

                    if (admin != null) {
                        session.setAttribute("admin", admin);
                        session.setAttribute("STORE_NAME", admin.getStoreName());
                    }
                }
            }

            // 2) 일반 회원일 경우
            else {
                if (session.getAttribute("member") == null) {

                    MemberVO member;

                    if (principal instanceof MemberVO) {
                        member = (MemberVO) principal;
                    } else {
                        // Remember-Me 상황 → DB에서 조회
                        member = memberMapper.findByEmail(loginId);
                    }

                    if (member != null) {
                        session.setAttribute("member", member);
                        session.setAttribute("LOGIN_USER_ID", member.getId());
                    }
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}