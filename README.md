# sKoped services

Scoped services, currently no hierarchy support.

Created to make it possible to define a scope that can be looked up within the Activity through the context chain, and share data/state between views who have the same scope tag.

## What's it like?

``` kotlin
class SomeKey : ScopeKey {
    override val scopeTag 
      get() = "Hello"
      
    override val serviceBinder
      get() = {
          add(MyService())
      }
}
      
class MyActivity: AppCompatActivity(), ServiceHost {
    override val services = viewModel.services
    
    fun something() {
        someKey.bindServices(services)
        
class MyView: SomeView(@JvmOverloads...) {
    override fun onFinishInflate() {
        super.onFinishInflate()
        if(!isInEditMode) {
            val myService = context.lookup<MyService>(getKey())
```

## Using sKoped Services

    allprojects {
        repositories {
            maven {
                url 'https://github.com/Zhuinden/skoped-services/raw/master/maven-repo'
            }
        }
    }


and add the compile dependency to your module level gradle.

    compile 'com.github.Zhuinden:skoped-services:0.0.2'


## Deployment memo

```
gradlew assemble 
gradlew :skoped-services:publishMavenPublicationToMavenRepository
```


## License

    Copyright 2018 Gabor Varadi

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
