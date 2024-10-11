package com.project.parkrental.controller;

import com.project.parkrental.inquiryBoard.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class InquiryController {

    @Autowired
    InquiryService inquiryService;

    //전체 목록 보기
    @GetMapping("/guest/inquiryList")
    public String inquiryList(@PageableDefault(size = 10) Pageable pageable, Model model) {
        Page<InquiryResponseDTO> inquiryResponseDTOs = inquiryService.findAll(pageable);
        model.addAttribute("inquiries", inquiryResponseDTOs);
        return "guest/inquiryList"; // Thymeleaf 템플릿 이름
    }

    //상세 뷰
    @GetMapping("/user/inquiryView")
    public String inquiryView(Model model, @RequestParam("idx")Long idx){
       model.addAttribute("inquiries", inquiryService.inquiryView(idx));
        return "user/inquiryView";
    }

    //글쓰기 란
    @GetMapping("/user/inquiryWrite")
    public String inquiryWriteForm(Model model){
        model.addAttribute("inquiryCreate", new InquiryRequestDTO());
        return "user/inquiryWrite";
    }

    //수정 란
    @GetMapping("/user/inquiryEdit/{idx}")
    public String inquiryEditForm(Model model, @PathVariable("idx") Long idx){
        Inquiry updatePost = inquiryService.inquiryView(idx);
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
            inquiryService.inquiryUpdate(idx, inquiryRequest, file, request);
            model.addAttribute("message", "게시글이 성공적으로 업데이트되었습니다.");
        } catch (EntityNotFoundException e) {
            model.addAttribute("message", e.getMessage());
            return "user/inquiryEdit"; // 해당 게시글이 없을 경우 다시 폼으로
        } catch (IOException | ServletException e) {
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

    @GetMapping("/user/inquiryDelete/{idx}")
    @ResponseBody
    public Map<String, String> inquiryDelete(@PathVariable("idx") Long idx){
        inquiryService.inquiryDelete(idx);

        Map<String, String> response = new HashMap<>();
        response.put("message", "글이 성공적으로 삭제되었습니다.");
        response.put("searchUrl", "/guest/inquiryList");

        return response; // Map을 JSON으로 변환하여 반환
    }
}
