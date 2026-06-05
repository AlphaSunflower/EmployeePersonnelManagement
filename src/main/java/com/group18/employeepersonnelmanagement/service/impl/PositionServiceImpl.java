package com.group18.employeepersonnelmanagement.service.impl;

import com.group18.employeepersonnelmanagement.entity.Position;
import com.group18.employeepersonnelmanagement.mapper.PositionMapper;
import com.group18.employeepersonnelmanagement.service.PositionService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PositionServiceImpl implements PositionService {

    private final PositionMapper positionMapper;

    @Override
    public List<Position> listByDeptId(Long deptId) {
        return positionMapper.selectList(
            new LambdaQueryWrapper<Position>().eq(Position::getDeptId, deptId)
        );
    }

    @Override
    public List<Position> listAll() {
        return positionMapper.selectList(null);
    }

    @Override
    public Position getById(Long id) {
        return positionMapper.selectById(id);
    }

    @Override
    public void save(Position position) {
        positionMapper.insert(position);
    }

    @Override
    public void update(Position position) {
        positionMapper.updateById(position);
    }

    @Override
    public void delete(Long id) {
        positionMapper.deleteById(id);
    }
}
