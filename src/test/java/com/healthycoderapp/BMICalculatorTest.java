package com.healthycoderapp;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class BMICalculatorTest {

	private String enviroment = "prod";
	
	@BeforeAll
	static void beforeAll() {
		System.out.println("Before all unit testes.");
	}
	
	@AfterAll
	static void agterAll() {
		System.out.println("After all unit testes.");
	}
	
	@Nested
	class IsDietRecommendedTests{
		@ParameterizedTest(name = "weight={0}, height{1}")
		@CsvFileSource(resources = "/diet-recommended-input-data.csv", numLinesToSkip = 1)
		void should_ReturnTrue_When_DietRecommended(Double coderWeight, Double coderHeight) {
			
			//given
			double weight = coderWeight;
			double height = coderHeight;
			
			//when
			boolean recommended = BMICalculator.isDietRecommended(weight, height);
			
			//then
			assertTrue(recommended);
		}
		
		@Test
		void should_ReturnFalse_When_DietRecommended() {
			
			//given
			double weight = 50.0;
			double height = 1.92;
			
			//when
			boolean recommended = BMICalculator.isDietRecommended(weight, height);
			
			//then
			assertFalse(recommended);
		}

		@Test
		//assertThrows() - allows you to test multiple exceptions within the same test
		void should_ThrowArithmeticException_When_DietRecommended() {
			
			//given
			double weight = 50.0;
			double height = 0.0;
			
			//when
			Executable excutable = () -> BMICalculator.isDietRecommended(weight, height);
			
			//then
			assertThrows(ArithmeticException.class, excutable);
		}
	}
//	@ParameterizedTest
//	@ValueSource(doubles = {89.0, 95.0, 110})
//	void should_ReturnTrue_When_DietRecommended(Double coderWeight) {
//		
//		//given
//		double weight = coderWeight;
//		double height = 1.72;
//		
//		//when
//		boolean recommended = BMICalculator.isDietRecommended(weight, height);
//		
//		//then
//		assertTrue(recommended);
//	}
	
//	@ParameterizedTest(name = "weight={0}, height{1}")
//	@CsvSource(value = {"89.0,1.72", "95.0, 1.75", "110.0, 1.78"})
//	void should_ReturnTrue_When_DietRecommended(Double coderWeight, Double coderHeight) {
//		
//		//given
//		double weight = coderWeight;
//		double height = coderHeight;
//		
//		//when
//		boolean recommended = BMICalculator.isDietRecommended(weight, height);
//		
//		//then
//		assertTrue(recommended);
//	}
	

	
	@Nested
	class FindCoderWithWorstBMITests{
		
		@Test
		@DisplayName(">>> sample method display name")
		//@Disabled
		@DisabledOnOs(OS.LINUX)
		void should_ReturnNulWorstBMI_When_CorderListNotEmpty() {
			
			//given
			List<Coder> coders = new ArrayList<>();
			
			//when
			Coder coderWorstBMI = BMICalculator.findCoderWithWorstBMI(coders);
			
			//then
			assertNull(coderWorstBMI);
		}
		
		@Test
		void should_ReturnCoderWithWorstBMI_CorderListNotEmpty() {
			
			//given
			List<Coder> coders = new ArrayList<>();
			coders.add(new Coder(1.80, 60.0));
			coders.add(new Coder(1.82, 98.0));
			coders.add(new Coder(1.82, 64.7));
			
			//when
			Coder coderWorstBMI = BMICalculator.findCoderWithWorstBMI(coders);
			
			//then
			assertAll(
					() -> assertEquals(1.82, coderWorstBMI.getHeight()),
					() -> assertEquals(98.0, coderWorstBMI.getWeight())
			);
		}
		
		@Test
		void should_ReturnCoderWithWorstBMIIn1Ms_When_CorderListHas10000Elements() {
			//given
			//assumeTrue() method validates the given assumption to be true and if the assumption is true – the test proceed, otherwise, test execution is aborted.
			Assumptions.assumeTrue(BMICalculatorTest.this.enviroment.equals("prod"));
			List<Coder> coders = new ArrayList<>();
			for (int i = 0; i < 10000; i++) {
				coders.add(new Coder(1.0 + i, 10.0 +i));
			}
			
			//when
			Executable excutable = () -> BMICalculator.findCoderWithWorstBMI(coders);
			
			//then
			assertTimeout(Duration.ofMillis(500), excutable);
		}
	}

	
	@Nested 
	class GetBMIScoresTests{
		@Test
		void should_ReturnCorrectBMIScoreArray_When_CorderListNotEmpty() {
			
			//given
			List<Coder> coders = new ArrayList<>();
			coders.add(new Coder(1.80, 60.0));
			coders.add(new Coder(1.82, 98.0));
			coders.add(new Coder(1.82, 64.7));
			double[] expected = {18.52, 29.59, 19.53};
			
			//when
			double[] bmiScores = BMICalculator.getBMIScores(coders);
			
			//then
			assertArrayEquals(expected, bmiScores);
		}
	}
	

	
}
