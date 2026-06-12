package com.example.studentapi.service.impl;

import com.example.studentapi.dto.StudentRequestDTO;
import com.example.studentapi.dto.StudentResponseDTO;
import com.example.studentapi.entity.Student;
import com.example.studentapi.exception.DuplicateEmailException;
import com.example.studentapi.exception.ResourceNotFoundException;
import com.example.studentapi.repository.StudentRepository;
import com.example.studentapi.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Override
    public StudentResponseDTO createStudent(StudentRequestDTO requestDTO) {
        if (studentRepository.existsByEmail(requestDTO.getEmail())) {
            throw new DuplicateEmailException(
                    "A student with email '" + requestDTO.getEmail() + "' already exists.");
        }

        Student student = mapToEntity(requestDTO);
        Student savedStudent = studentRepository.save(student);
        return mapToResponseDTO(savedStudent);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponseDTO> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponseDTO getStudentById(Long id) {
        Student student = findStudentOrThrow(id);
        return mapToResponseDTO(student);
    }

    @Override
    public StudentResponseDTO updateStudent(Long id, StudentRequestDTO requestDTO) {
        Student existing = findStudentOrThrow(id);

        if (!existing.getEmail().equalsIgnoreCase(requestDTO.getEmail())
                && studentRepository.existsByEmail(requestDTO.getEmail())) {
            throw new DuplicateEmailException(
                    "Email '" + requestDTO.getEmail() + "' is already in use by another student.");
        }

        existing.setFirstName(requestDTO.getFirstName());
        existing.setLastName(requestDTO.getLastName());
        existing.setEmail(requestDTO.getEmail());
        existing.setAge(requestDTO.getAge());
        existing.setCourse(requestDTO.getCourse());

        Student updatedStudent = studentRepository.save(existing);
        return mapToResponseDTO(updatedStudent);
    }

    @Override
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Student not found with id: " + id);
        }
        studentRepository.deleteById(id);
    }

    private Student findStudentOrThrow(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Student not found with id: " + id));
    }

    private Student mapToEntity(StudentRequestDTO dto) {
        return Student.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .age(dto.getAge())
                .course(dto.getCourse())
                .build();
    }

    private StudentResponseDTO mapToResponseDTO(Student student) {
        return StudentResponseDTO.builder()
                .id(student.getId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .age(student.getAge())
                .course(student.getCourse())
                .build();
    }
}
