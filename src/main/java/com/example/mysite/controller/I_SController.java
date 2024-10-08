package com.example.mysite.controller;

import com.example.mysite.classes.*;
import com.example.mysite.service.I_SService;
import com.example.mysite.service.IdentityService;
import com.example.mysite.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/iss")
public class I_SController {
    @Autowired
    private I_SService i_sService;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private SkillService skillService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping
    public List<I_S> getAllISs() {
        return  i_sService.getALLISs();
    }

    @GetMapping("/{identityId}/{skillId}")
    public I_S getIDById(@PathVariable Integer identityId, @PathVariable Integer skillId) {
        I_S.I_SKey i_sKey = new I_S.I_SKey();
        i_sKey.setIdentity_id(identityId);
        i_sKey.setSkill_id(skillId);
        return i_sService.getISById(i_sKey);
    }

    @PostMapping
    public I_S createIS(@RequestBody I_S i_s) {
        return i_sService.saveIS(i_s);
    }

    @PostMapping("/create")
    public I_S createISByIds(@RequestParam Integer identity_id, @RequestParam Integer skill_id) {
        I_S.I_SKey i_sKey = new I_S.I_SKey();
        i_sKey.setIdentity_id(identity_id);
        i_sKey.setSkill_id(skill_id);

        I_S i_s = new I_S();
        i_s.setI_sKey(i_sKey);

        Identity identity = identityService.getIdentityById(identity_id);
        Skill skill = skillService.getSkillById(skill_id);

        i_s.setIdentity(identity);
        i_s.setSkill(skill);

        return i_sService.saveIS(i_s);
    }

    @DeleteMapping("/{identityId}/{skillId}")
    public void deleteID(@PathVariable Integer identityId, @PathVariable Integer skillId) {
        I_S.I_SKey i_sKey = new I_S.I_SKey();
        i_sKey.setIdentity_id(identityId);
        i_sKey.setSkill_id(skillId);
        i_sService.deleteIS(i_sKey);
    }

    @DeleteMapping("/delete")
    public I_S deleteISByS(@RequestParam Integer skill_id) {
        String selectSql = "SELECT * FROM I_S WHERE skill_id = ?";
        I_S i_s = jdbcTemplate.queryForObject(selectSql, new BeanPropertyRowMapper<>(I_S.class), skill_id);

        if (i_s != null) {
            String deleteSql = "DELETE FROM I_S WHERE skill_id = ?";
            jdbcTemplate.update(deleteSql, skill_id);
        }

        return i_s;
    }

    @DeleteMapping("/delete/i")
    public List<I_S> deleteISByI(@RequestParam Integer identity_id) {
        String selectSql = "SELECT * FROM I_S WHERE identity_id = ?";
        List<I_S> i_ss = jdbcTemplate.query(selectSql, new BeanPropertyRowMapper<>(I_S.class), identity_id);

        if (i_ss != null) {
            String deleteSql = "DELETE FROM I_S WHERE identity_id = ?";
            jdbcTemplate.update(deleteSql, identity_id);
        }

        return i_ss;
    }
}
