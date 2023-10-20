package hr.solujic.leapwise.repository;

import hr.solujic.leapwise.entity.Expression;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpressionRepository extends JpaRepository<Expression, Integer> {}
