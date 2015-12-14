/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package genetic;

import java.util.ArrayList;

import org.jenetics.Chromosome;
import org.jenetics.DoubleChromosome;
import org.jenetics.DoubleGene;
import org.jenetics.Optimize;
import org.jenetics.Phenotype;
import org.jenetics.SinglePointCrossover;
import org.jenetics.SwapMutator;
import org.jenetics.engine.Engine;
import org.jenetics.engine.EvolutionResult;
import org.jenetics.engine.EvolutionStatistics;

import terrain.Position;

import static org.jenetics.engine.limit.bySteadyFitness;


/**
 *
 * @author isuru
 */
public class PathPlanner {
    
    static double minimumGeneValue = -10;
    static double maximumGeneValue = 10;
    private static final int numOfIterations = 15;
    
    public static void calculateFuturePositions(Position [] currentPositions, Position initialPosition, Position targetPosition)
    {
        MyFitnessFunction ff = new MyFitnessFunction(currentPositions,  initialPosition, targetPosition);
        final Engine<DoubleGene, Double> engine = Engine
			.builder(
				ff,
				DoubleChromosome.of(minimumGeneValue, maximumGeneValue, currentPositions.length * 2))
			.optimize(Optimize.MAXIMUM)
			.maximalPhenotypeAge(numOfIterations)
			.populationSize(1000)
			.alterers(
				new SwapMutator<>(0.25),
				new SinglePointCrossover<>(0.35))
			.build();

		// Create evolution statistics consumer.
		final EvolutionStatistics<Double, ?>
			statistics = EvolutionStatistics.ofNumber();

		final Phenotype<DoubleGene, Double> best =
			engine.stream()
			// Truncate the evolution stream after 7 "steady"
			// generations.
			.limit(bySteadyFitness(15))
			// The evolution will stop after maximal 100
			// generations.
			.limit(1000)
			// Update the evaluation statistics after
			// each generation
			.peek(statistics)
			// Collect (reduce) the evolution stream to
			// its best phenotype.
			.collect(EvolutionResult.toBestPhenotype());

		System.out.println(statistics);
		System.out.println(best);
		
		final Chromosome<DoubleGene> chromosome = best.getGenotype().getChromosome();
		
        for (int i = 0; i < chromosome.length() / 2; ++i) 
        {
        	currentPositions[i] = (new Position(currentPositions[i].getX() + chromosome.getGene(i * 2).getAllele(), currentPositions[i].getY() + chromosome.getGene((i * 2) + 1).getAllele(), 0));              
        }
        
    }
    
    public static void calculateNextPosition(Position currentPosition, Position initialPosition, Position targetPosition)
    {
    	System.out.println("PathPlanner -> inside of calculateNextPosition");
    	//update current position
    }
}
