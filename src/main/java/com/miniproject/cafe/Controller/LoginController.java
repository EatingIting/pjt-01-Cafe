package com.miniproject.cafe.Controller;

//import com.miniproject.cafe.Service.MemberService;
import com.miniproject.cafe.VO.MemberVO;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/login")
public class LoginController {

//    @Autowired
//    private MemberService memberService;

    // 로그인 페이지
    @GetMapping("/")
    public String loginPage() {
        return "login";
    }

    // 로그인 처리
    @PostMapping("/loginForm")
    public String loginForm(MemberVO vo, HttpSession session, Model model) {
        return "login/loginForm";
    }

////        MemberVO member = memberService.login;
//
//        if (member != null) {
//            session.setAttribute("loginMember", member);
//            return "redirect:/main"; // 로그인 성공 시 메인으로 이동
//        } else {
//            model.addAttribute("msg", "아이디 또는 비밀번호가 올바르지 않습니다.");
//            return "login/login"; // 다시 로그인 화면
//        }
////    }
//
//    // 로그아웃
//    @GetMapping("/logout")
//    public String logout(HttpSession session) {
//        session.invalidate(); // 세션 전체 삭제
//        return "redirect:/login/"; // 로그인 화면으로 이동
//    }
}
