package de.tud.stg;

import java.io.IOException;
import java.util.StringTokenizer;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/** Task 3 - Tran Dinh Khuong, 23110035. Ten characters belong to big, as in the solution. */
public class WordLength extends Configured implements Tool {
    public static class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable> {
        private static final IntWritable ONE = new IntWritable(1);
        private final Text category = new Text();

        @Override
        protected void map(Object key, Text value, Context context)
                throws IOException, InterruptedException {
            StringTokenizer tokenizer = new StringTokenizer(value.toString(), ",;\\. \t\n\r\f");
            while (tokenizer.hasMoreTokens()) {
                String word = tokenizer.nextToken();
                int length = word.length();
                String c = length == 1 ? "tiny"
                        : length >= 2 && length <= 4 ? "small"
                        : length >= 5 && length <= 9 ? "medium" : "big";
                category.set(c);
                context.write(category, ONE);
            }
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
            System.err.println("Usage: WordLength [generic options] <input> <output>");
            return 2;
        }
        Job job = Job.getInstance(getConf(), "Team MapReduce - WordLength - Dinh Khuong 23110035");
        job.setJarByClass(WordLength.class);
        job.setMapperClass(TokenizerMapper.class);
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
        System.exit(ToolRunner.run(new WordLength(), args));
    }
}
