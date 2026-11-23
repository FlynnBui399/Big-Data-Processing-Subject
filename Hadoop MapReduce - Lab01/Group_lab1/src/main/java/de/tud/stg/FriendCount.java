package de.tud.stg;

import java.io.IOException;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/** Task 2: count the first person's friendship records; input includes both directions. */
public class FriendCount extends Configured implements Tool {
    public static class FriendMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
        private static final IntWritable ONE = new IntWritable(1);
        private final Text person = new Text();

        @Override
        protected void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {
            String line = value.toString().trim();
            if (line.isEmpty()) return;
            String[] pair = line.split(",", -1);
            if (pair.length != 2 || pair[0].trim().isEmpty() || pair[1].trim().isEmpty()) {
                throw new IOException("Expected one friendship pair: person,friend");
            }
            person.set(pair[0].trim());
            context.write(person, ONE);
        }
    }

    public static class IntSumReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        private final IntWritable result = new IntWritable();

        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context)
                throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable value : values) sum += value.get();
            result.set(sum);
            context.write(key, result);
        }
    }

    @Override
    public int run(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: FriendCount [generic options] <input> <output>");
            return 2;
        }
        Job job = Job.getInstance(getConf(), "Team MapReduce - FriendCount");
        job.setJarByClass(FriendCount.class);
        job.setMapperClass(FriendMapper.class);
        job.setCombinerClass(IntSumReducer.class);
        job.setReducerClass(IntSumReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        job.setNumReduceTasks(1);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        System.exit(ToolRunner.run(new FriendCount(), args));
    }
}
