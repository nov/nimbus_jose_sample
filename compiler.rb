module Compiler
  module_function

  def exec(klass)
    compile klass
    Dir.chdir(current_dir) do
      command = "java -classpath #{jar_file_paths.join(':')}:. #{klass}"
      res = `#{command}`
      puts res
    end
  end

  def compile(klass)
    unless @compiled
      Dir.chdir(current_dir) do
        command = "javac -classpath #{jar_file_paths.join(':')} #{klass}.java"
        `#{command}`
      end
      @compiled = true
    end
  end

  def current_dir
    File.dirname(__FILE__)
  end

  def jar_file_paths
    Dir.glob './*.jar'
  end
end

Compiler.exec ARGV[0]