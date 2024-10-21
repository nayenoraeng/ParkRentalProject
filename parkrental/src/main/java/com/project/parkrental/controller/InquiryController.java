package com.project.parkrental.controller;

import com.project.parkrental.inquiryBoard.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
public class InquiryController {

    @Autowired
    InquiryService inquiryService;

    //전체 목록 보기
    @GetMapping("/guest/inquiryList")
    public String inquiryList(@PageableDefault(size = 10, sort = "postdate", direction = Sort.Direction.DESC) Pageable pageable,
                              @RequestParam(value = "keyword", required = false) String keyword,
                              Model model) {
        Page<InquiryResponseDTO> inquiryResponseDTOs;
        if (keyword != null && !keyword.trim().isEmpty()) {
            inquiryResponseDTOs = inquiryService.searchInquiries(keyword, pageable);
        } else {
            inquiryResponseDTOs = inquiryService.findAll(pageable);
        }

        model.addAttribute("inquiries", inquiryResponseDTOs);
        model.addAttribute("keyword", keyword);  // 검색어를 모델에 추가
        return "guest/inquiryList"; // Thymeleaf 템플릿 이름
    }

//    //전체 목록 보기
//    @GetMapping("/guest/inquiryList")
//    public String inquiryList(@PageableDefault(size = 10, sort = "postdate", direction = Sort.Direction.DESC) Pageable pageable,
//                              Model model) {
//        Page<InquiryResponseDTO> inquiryResponseDTOs = inquiryService.findAll(pageable);
//        model.addAttribute("inquiries", inquiryResponseDTOs);
//        return "guest/inquiryList"; // Thymeleaf 템플릿 이름
//    }

    //비밀번호 확인창
    @RequestMapping("/user/inquiryPass")
    public String inquiryPass(HttpServletRequest request, Model model){
        Long idx = Long.valueOf(request.getParameter("idx"));
        Inquiry inquiry = inquiryService.inquiryView(idx);

        model.addAttribute("inquiry", inquiry);
        return "user/inquiryPass";
    }

    // 비밀번호 확인
    @PostMapping("/user/inquiryViewPro")
    public String inquiryPassPro(HttpServletRequest request, Model model) {

        String sId = SecurityContextHolder.getContext().getAuthentication().getName();
        String password = request.getParameter("password");
        Long idx = Long.valueOf(request.getParameter("idx"));
        Inquiry inquiry = inquiryService.inquiryView(idx);

        if(inquiry != null && inquiry.getInquiryPassword().equals(password)){
            inquiryService.inquiryUpdateViewCount(idx);
            model.addAttribute("inquiries", inquiry);
            return "user/inquiryView";
        } else {
            model.addAttribute("error", "비밀번호가 맞지 않습니다.");
            model.addAttribute("Id", sId);
            model.addAttribute("idx", idx); // 다시 idx를 전달하여 다시 시도 가능하게 함
            return "user/inquiryPass";
        }
    }

    //상세 뷰
    @RequestMapping("/user/inquiryView")
    public String inquiryBoardView(HttpServletRequest request, Model model) {

        String sId = SecurityContextHolder.getContext().getAuthentication().getName();
        Long idx = Long.valueOf(request.getParameter("idx"));
        Inquiry inquiry = inquiryService.inquiryView(idx);

        return "user/inquiryView";
    }

    //글쓰기 란
    @GetMapping("/user/inquiryWrite")
    public String inquiryWriteForm(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // 현재 로그인한 사용자 이름
        model.addAttribute("username", username);
        return "user/inquiryWrite";
    }

    //수정 란
    @GetMapping("/user/inquiryEdit/{idx}")
    public String inquiryEditForm(Model model, @PathVariable("idx") Long idx){

        Inquiry updatePost = inquiryService.inquiryView(idx);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // 현재 로그인한 사용자 이름

        model.addAttribute("username", username);
        model.addAttribute("inquiryRequest", updatePost);

        return "user/inquiryEdit";
    }

    //수정하기
    @PostMapping("/user/inquiryEditPro")
    public String inquiryEditPro(@RequestParam("idx") Long idx,
                                 @ModelAttribute("inquiryRequest") @Valid InquiryRequestDTO inquiryRequest,
                                 BindingResult bindingResult,
                                 @RequestPart(value = "file", required = false) MultipartFile file,
                                 HttpServletRequest request,
                                 Model model){
        if (bindingResult.hasErrors()){
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "user/inquiryEdit"; // 오류 발생 시 폼으로 돌아감
        }

        try {
            inquiryService.inquiryUpdate(idx, inquiryRequest, file);
            model.addAttribute("message", "게시글이 성공적으로 업데이트되었습니다.");
        } catch (EntityNotFoundException e) {
            model.addAttribute("message", e.getMessage());
            return "user/inquiryEdit"; // 해당 게시글이 없을 경우 다시 폼으로
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("message", "게시글 업데이트 중 오류가 발생했습니다.");
            return "user/inquiryEdit"; // 오류 발생 시 폼으로 돌아감
        }

        return "redirect:/guest/inquiryList"; // 목록 페이지로 리다이렉트
    }


    //글쓰기
    @PostMapping("/user/inquiryWritePro")
    public String inquiryWritePro(@ModelAttribute("inquiryCreate") @Valid InquiryRequestDTO inquiryCreate, BindingResult bindingResult,
                                  @RequestPart(value="file", required = false) MultipartFile file,
                                  HttpServletRequest request, Model model) throws FileNotFoundException {

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            //model.addAttribute("message", "입력한 내용에 오류가 있습니다.");
            return "user/inquiryWrite"; // 오류 발생 시 폼으로 돌아감
        }

        try {

            if (file != null && !file.isEmpty()) {
                String ofile = file.getOriginalFilename();
                String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/files";// 이미지 저장 경로 지정
                System.out.println(uploadDir);

                File dir = new File(uploadDir);
                if (!dir.exists()) {
                    dir.mkdir();
                }

                String sfile = UUID.randomUUID().toString() + "_" + ofile;

                File destination = new File(dir, sfile);
                file.transferTo(destination);

                // 파일이 있을 경우에만 DTO에 이미지 설정
                inquiryCreate.setOfile(ofile);
                inquiryCreate.setSfile(sfile);
            } else {
                // 파일이 없을 때 처리할 내용 (필요한 경우)
                System.out.println("No file uploaded.");
            }

            inquiryService.inquiryWrite(inquiryCreate, request); // inquiryWrite 호출
            model.addAttribute("message", "글이 성공적으로 등록되었습니다.");


        } catch (IOException | ServletException e) {
            e.printStackTrace();
            model.addAttribute("message", "파일 업로드 중 오류가 발생했습니다.");
            return "user/inquiryWrite";
        }

        return "redirect:/guest/inquiryList"; // 목록 페이지로 리다이렉트
    }

    //글 삭제하기
    @GetMapping("/user/inquiryDelete/{idx}")
    @ResponseBody
    public Map<String, String> inquiryDelete(@PathVariable("idx") Long idx){
        inquiryService.inquiryDelete(idx);

        Map<String, String> response = new HashMap<>();
        response.put("message", "글이 성공적으로 삭제되었습니다.");
        response.put("searchUrl", "/guest/inquiryList");

        return response; // Map을 JSON으로 변환하여 반환
    }

    //답글 란
    @GetMapping("/user/inquiryReply/{idx}")
    public String inquiryReplyForm(Model model, @PathVariable("idx") Long idx){
        inquiryService.inquiryView(idx);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // 현재 로그인한 사용자 이름
        model.addAttribute("username", username);

        return "user/inquiryReply";
    }


}
