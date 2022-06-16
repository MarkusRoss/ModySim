package ar.edu.unsl.mys.utils;

import java.util.Random;


public class CustomRandomizer implements Randomizer
{
    private static CustomRandomizer customRandomizer;

    private Random randomizer;

    private CustomRandomizer()
    {
        this.randomizer = new Random(191918);//191919);//Seed fija
        //Seed dinamica System.currentTimeMillis()
    }

    public static CustomRandomizer getInstance()
    {
        if(CustomRandomizer.customRandomizer == null)
            CustomRandomizer.customRandomizer = new CustomRandomizer();
        return CustomRandomizer.customRandomizer;
    }

    @Override
    public double nextRandom()
    {
        return randomizer.nextDouble();
    }

    @Override
    public float nextGaussiano(int mean, double stddev){
        int n = 12;
        int s = 1;
        double conv = 0;
        for(int i = 0; i < n; i++){
            conv += nextRandom();
        }
        double z = (conv - (n/2)) /s;
        return (float)(z * stddev + mean);
    }

    @Override
    public float nextExponencial(int mean){
        double r = nextRandom();
        return (float)(-mean * Math.log(1 - r));
    }   

    @Override
    public float nextUniforme(int a, int b){
        return (float)(a + (b-a) * nextRandom());
    }
}