package com.elearning.repository;

import com.elearning.model.Instruction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstructionRepository extends JpaRepository<Instruction, Long> {

    Instruction findInstructionById(long id);
    List<Instruction> findInstructionsByUserId(long userId);
    List<Instruction> findInstructionsByCourseId(long courseId);
}
