/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package genetic;

import java.io.Serializable;
import java.util.function.Function;

import org.jenetics.Chromosome;
import org.jenetics.DoubleGene;
import org.jenetics.Genotype;

import terrain.Position;
import terrain.PositionUtil;

/**
 *
 * @author isuru
 */

final class MyFitnessFunction implements Function<Genotype<DoubleGene>, Double>, Serializable {
	private static final long serialVersionUID = 1L;
	private Position[] currentPositions;
	Position initialPosition;
	Position targetPosition;
	double vehicleRadius = 10.0;
	double maxNavDisPerStep = 10.0;
	int dimensions = 2;
	int numOfConstrains = 3;

	public MyFitnessFunction(Position[] currentPositions, Position initialPosition, Position targetPosition) {
		this.currentPositions = currentPositions;
		this.initialPosition = initialPosition;
		this.targetPosition = targetPosition;
	}

	@Override
	public Double apply(final Genotype<DoubleGene> genotype) {
		Double retVal = 1000.0;

		final Chromosome<DoubleGene> chromosome = genotype.getChromosome();
		int length = chromosome.length();
		Position positions[] = new Position[length / dimensions];

		for (int i = 0; i < length / dimensions; ++i) {
			positions[i] = new Position(currentPositions[i].getX() + chromosome.getGene(i * dimensions).getAllele(),
					currentPositions[i].getY() + chromosome.getGene((i * dimensions) + 1).getAllele(), 0);
		}

		// check inter agent collision
		for (int i = 0; i < positions.length; i++) {
			for (int j = 0; j < positions.length; j++) {
				if (i != j) {
					if (positions[i].getDistance(positions[j]) < vehicleRadius * 2.2) {
//						retVal = retVal - 50 * ((positions[i].getDistance(positions[j]))
//								/ (numOfConstrains * (positions.length - 1)));
						return 0.0;
					} else {
						retVal = retVal + 1 / (((positions[i].getDistance(positions[j]))
								/ (numOfConstrains * 10 * (positions.length - 1))));
					}
				}
			}
		}

		double change = 0;
//		// check smooth navigation by movement of swarm cent
//		change = PositionUtil.getCenter(positions).getDistance(PositionUtil.getCenter(currentPositions));
//
//		if (change > maxNavDisPerStep) {
//			retVal = retVal + 1 / ((change / (numOfConstrains * 1000)));
//		} else {
//			retVal = retVal + (change / (numOfConstrains * 1000));
//		}

		// check moving out from initial position
		change = PositionUtil.getCenter(positions).getDistance(initialPosition);
		retVal = retVal + (change / (numOfConstrains));

		// check check moving in to target position
		change = PositionUtil.getCenter(positions).getDistance(targetPosition);
		retVal = retVal - (change / (numOfConstrains));


//		// check the smooth navigation by movement of single agent wise
//		for (int i = 0; i < positions.length; i++) {
//			System.out.println("DDDDDDDDDDDDDDDDDDDDDD  " + currentPositions[i].getDistance(positions[i]));
//			if (currentPositions[i].getDistance(positions[i]) > maxNavDisPerStep) {
//				//retVal = retVal + 1 / ((currentPositions[i].getDistance(positions[i]) / (numOfConstrains * positions.length * 800)));
//				return 0.0;
//			} else {
//				retVal = retVal + (currentPositions[i].getDistance(positions[i]) / (numOfConstrains * positions.length * 500));
//			}
//
//		}
//		
//		// check the smooth navigation by movement of single agent wise
//				for (int i = 0; i < positions.length; i++) {
//					retVal = retVal - (currentPositions[i].getDistance(targetPosition) / (numOfConstrains * 500));
//				}

		return retVal;

	}
}
