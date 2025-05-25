#!/bin/bash

# Script to run JMH CounterBenchmark with different thread configurations
# Usage: ./run-counter-benchmarks.sh

JAR_PATH="code/build/libs/code-jmh.jar"
BENCHMARK_PATTERN="CounterBenchmark"
REPORTS_DIR="build/jmh-reports"

# Thread configurations: producer,consumer threads
THREAD_CONFIGS=("1,1" "1,7" "4,4" "7,1")

# Check if JAR exists
if [ ! -f "$JAR_PATH" ]; then
    echo "Error: JMH JAR not found at $JAR_PATH"
    echo "Please build the project first with: ./gradlew jmhJar"
    exit 1
fi

# Create reports directory
mkdir -p "$REPORTS_DIR"

echo "Running JMH benchmarks for pattern: $BENCHMARK_PATTERN"
echo "JAR: $JAR_PATH"
echo "Thread configurations: ${THREAD_CONFIGS}"
echo "=========================================="

./gradlew clean jmhJar

for config in "${THREAD_CONFIGS[@]}"; do
    echo ""
    echo "Running with thread configuration: $config"
    echo "----------------------------------------"
    
    # Create filename with thread configuration
    config_name=$(echo "$config" | tr ',' '-')
    output_file="$REPORTS_DIR/counter-benchmark-threads-${config_name}.csv"
    
    java -jar "$JAR_PATH" \
        -i 1 \
        -wi 1 \
        -f 1 \
        -tg "$config" \
        -rf csv \
        -rff "$output_file" \
        "$BENCHMARK_PATTERN"
    
    echo "Completed configuration: $config"
    echo "Results saved to: $output_file"
done

echo ""
echo "All benchmark runs completed!"