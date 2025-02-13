package com.example.smartguide.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.smartguide.config.JwtTokenUtil;
import com.example.smartguide.dto.RecordReq;
import com.example.smartguide.dto.RecordRes;
import com.example.smartguide.mapper.BeaconMapper;
import com.example.smartguide.mapper.BuildingMapper;
import com.example.smartguide.mapper.MemberMapper;
import com.example.smartguide.mapper.RecordMapper;
import com.example.smartguide.model.Building;
import com.example.smartguide.model.Record;

@Service
public class RecordService {
	
	@Autowired
	private RecordMapper recordMapper;
	
	@Autowired
	private BeaconMapper beaconMapper;
	
	@Autowired
	private MemberMapper memberMapper;
	
	@Autowired
	private BuildingMapper buildingMapper;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	public List<Record> getUserRecords(Long userId) {
		return recordMapper.selectUserRecord(userId);
	}
	
	public List<Building> getBuildingListByUsername(String username) {
		return buildingMapper.selectBuildingByUsername(username);
	}
	
	public List<RecordRes> getBuildingRecords(Long buildingId) {
		return recordMapper.selectBuildingRecord(buildingId);
	}
	
	public List<Record> getAllRecords() {
		return recordMapper.selectAllRecord();
	}
	
	public void setRecord(String token, RecordReq recordReq) {
		String username = jwtTokenUtil.getUsernameFromToken(token);
		Long userId = memberMapper.selectUserIdByUsername(username);
		
		Long beaconId = beaconMapper.getBeaconIdByUuidAndMajorAndMinor(recordReq.getUuid(), recordReq.getMajor(), recordReq.getMinor());
		
		Record record = new Record();
		record.setUserId(userId);
		record.setBeaconId(beaconId);
		record.setCreatedAt(recordReq.getCreatedAt());
		
		recordMapper.insertRecord(record);
	}
	
}
