package com.example.trainbuddy_server.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.trainbuddy_server.entity.Group;
import com.example.trainbuddy_server.entity.GroupMembers;
import com.example.trainbuddy_server.service.GroupMembersService;
import com.example.trainbuddy_server.service.GroupService;

@RestController
@RequestMapping("/api/groups")
public class GroupController {
    private final GroupService groupService;
    private final GroupMembersService membersService;
    public GroupController(GroupService groupService, GroupMembersService membersService) {
        this.groupService = groupService;
        this.membersService = membersService;
    }

    @GetMapping
    public List<Group> getAll() {
        return groupService.getAll();
    }

    @PostMapping
    public Group create(@RequestBody Group g) {
        return groupService.create(g);
    }

    @GetMapping("/{groupId}/members")
    public List<GroupMembers> getMembers(@PathVariable Long groupId) {
        return membersService.getByGroup(groupId);
    }

    @PostMapping("/{groupId}/members")
    public GroupMembers addMember(@PathVariable Long groupId, @RequestBody GroupMembers gm) {
        return membersService.add(gm);
    }
}