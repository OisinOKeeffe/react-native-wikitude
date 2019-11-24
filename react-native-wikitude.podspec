require 'json'

package = JSON.parse(File.read(File.join(__dir__, 'package.json')))

Pod::Spec.new do |s|
  s.name         = package['name']
  s.version      = package['version']
  s.summary      = package['description']
  s.license      = package['license']

  s.authors      = package['author']
  s.homepage     = 'n/a'
  s.platform     = :ios, "9.0"

  s.vendored_frameworks = 'WikitudeSDK.framework'

  s.source       = { :git => "https://github.com/reesenewslab/react-native-wikitude.git", :tag => "v#{s.version}" }
  s.source_files  = "ios/**/*.{h,m}"

  s.dependency 'React'
end