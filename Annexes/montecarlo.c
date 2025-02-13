#include <stdlib.h>
#include <windows.h>
#include <stdio.h>
#include <time.h>

typedef struct {
    int num_samples;
    int not_inside_circle;
    CRITICAL_SECTION *lock;
} ThreadArgs;

int total_not_inside_circle = 0;

double random_double() {
    unsigned int value;
    rand_s(&value); 
    return (double)value / UINT_MAX;
}

DWORD WINAPI monte_carlo_worker(LPVOID arg) {
    ThreadArgs *args = (ThreadArgs *)arg;
    int not_in_circle = 0;

    for (int i = 0; i < args->num_samples; i++) {
        double x = random_double(); 
        double y = random_double();
        if (x * x + y * y <= 1.0) {
            not_in_circle++;
        }
    }

    EnterCriticalSection(args->lock);
    total_not_inside_circle += not_in_circle;
    LeaveCriticalSection(args->lock);

    args->not_inside_circle = not_in_circle;
    return 0;
}

int main() {
    unsigned int total_samples = 10000000; 
    int num_threads = 8; 
    HANDLE threads[8];
    ThreadArgs thread_args[8];
    CRITICAL_SECTION lock;

    InitializeCriticalSection(&lock);

    int samples_per_thread = total_samples / num_threads;

    printf("Estimation de Pi avec %d échantillons et %d threads...\n", total_samples, num_threads);

    clock_t start_time = clock();

    for (int i = 0; i < num_threads; i++) {
        thread_args[i].num_samples = samples_per_thread;
        thread_args[i].not_inside_circle = 0;
        thread_args[i].lock = &lock;

        threads[i] = CreateThread(
            NULL,               
            0,                 
            monte_carlo_worker,  
            &thread_args[i],  
            0,               
            NULL 
        );
    }
    for (int i = 0; i < num_threads; i++) {
        WaitForSingleObject(threads[i], INFINITE);
        CloseHandle(threads[i]);
    }

    double estimated_pi = 4.0 * total_not_inside_circle / total_samples;

    clock_t end_time = clock();
    double execution_time = (double)(end_time - start_time) / CLOCKS_PER_SEC;

    printf("Valeur estimée de Pi : %f\n", estimated_pi);
    printf("Nombre total de calculs : %d\n", total_samples);
    printf("Temps d'exécution : %.2f secondes\n", execution_time);

    DeleteCriticalSection(&lock);

    return 0;
}