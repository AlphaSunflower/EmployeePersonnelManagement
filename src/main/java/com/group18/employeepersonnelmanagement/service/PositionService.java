package com.group18.employeepersonnelmanagement.service;

import com.group18.employeepersonnelmanagement.entity.Position;
import java.util.List;

public interface PositionService {
    List<Position> listByDeptId(Long deptId);
    List<Position> listAll();
    Position getById(Long id);
    void save(Position position);
    void update(Position position);
    void delete(Long id);
}
