package com.example.chatserver.member.service;

import com.example.chatserver.member.domain.Member;
import com.example.chatserver.member.dto.MemberListResDto;
import com.example.chatserver.member.dto.MemberLoginReqDto;
import com.example.chatserver.member.dto.MemberSaveReqDto;
import com.example.chatserver.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    public Member create(MemberSaveReqDto memberSaveReqDto) {
        //이미 가입되어 있는 이메일 검증
        if(memberRepository.findByEmail(memberSaveReqDto.getEmail()).isPresent()){
            throw new IllegalArgumentException("이미 존재하는 이메일 입니다");
        }
        Member newMember = Member.builder()
                .name(memberSaveReqDto.getName())
                .email(memberSaveReqDto.getEmail())
                .password(passwordEncoder.encode(memberSaveReqDto.getPassword()))
                .build();

        Member member = memberRepository.save(newMember);
        return member;
    }
    public Member login(MemberLoginReqDto memberLoginReqDto) {
        Member member = (Member) memberRepository.findByEmail(memberLoginReqDto.getEmail()).orElseThrow(() -> new EntityNotFoundException("존재하지 않은 이메일 입니다."));
        if(!passwordEncoder.matches(memberLoginReqDto.getPassword(), member.getPassword())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");

        }

        return member;
    }
    public List<MemberListResDto> findAll(){
        List<Member> members = memberRepository.findAll();
        List<MemberListResDto> memberListResDtos = new ArrayList<>();
        for (Member member : members) {
            MemberListResDto memberListResDto = new MemberListResDto();
            memberListResDto.setId(member.getId());
            memberListResDto.setEmail(member.getEmail());
            memberListResDto.setName(member.getName());
            memberListResDtos.add(memberListResDto);
        }
        return memberListResDtos;
    }

}
