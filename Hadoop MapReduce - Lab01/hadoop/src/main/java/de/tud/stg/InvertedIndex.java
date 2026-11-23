package de.tud.stg;

import java.io.IOException;
import java.util.StringJoiner;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class InvertedIndex {
    public static class FilenameReducer extends Reducer<Text, Text, Text, Text> {
        private final Text result = new Text();

        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context)
                throws IOException, InterruptedException {
            StringJoiner filenames = new StringJoiner(",");
            for (Text filename : values) {
                filenames.add(filename.toString());
            }
            result.set(filenames.toString());
            context.write(key, result);
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: InvertedIndex <input-path> <output-path>");
            System.exit(2);
        }

        Job job = Job.getInstance(new Configuration(), "InvertedIndex");
        job.setJarByClass(InvertedIndex.class);
        job.setMapperClass(Comprehension.TokenizerMapper.class);
        job.setReducerClass(FilenameReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
