require 'rubygems'
#require 'deltacloud-dependencies.jar'
require 'deltacloud-dependencies.jar'
#require 'steamcannon-deltacloud-core'

# Force Rubygems to reload the gem paths
Gem.clear_paths

version = ">= 0"

if ARGV.first =~ /^_(.*)_$/ and Gem::Version.correct? $1 then
  version = $1
  ARGV.shift
end

ARGV[0] = "-i mock"

load Gem.bin_path('steamcannon-deltacloud-core', 'deltacloudd', version)


