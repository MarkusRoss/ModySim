package ar.edu.unsl.mys.utils;

public interface Randomizer
{
    double nextRandom();
    float nextGaussiano(int mean, double stddev);
    float nextExponencial(int mean);
    float nextUniforme(int a, int b);
}