# Multiple Media Picker :boom: :star2:
### An android library to pick multiple images and/or videos from built-in gallery. This library is encouraged to use as little memory as possible. 

## NOTE: If you feel like this library can help you, feel free to fork or contribute creating pull requess (:

## Screenshot
![Layout](Images/mediapicker.PNG)
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

# Usage
Include easily in your project adding the dependency to your build.gradle file.  

```gradle
dependencies {
   implementation project(':multiplemediapicker')
}
```
# Getting started
In the activity from where you want to call the library, declare


 request permissions to read external storage

```
    SystemPermission.READ_USER_STORAGE) != IBundleManager.PERMISSION_GRANTED
```

Create the intent

```
  button.setClickedListener(listener ->
                openGallery());
  
  public void openGallery() {
    present(new HomeAbilitySlice(), new Intent());
  }
```

Added Viewpager
```
 
 MultiplePageSliderProvider adapter = new MultiplePageSliderProvider(this);
        adapter.addFragment("Images");
        adapter.addFragment("Videos");
        viewPager.setProvider(adapter);
```

## Custom styles

The primary colors will be inherited from the project it was called. But you can customize a little more by adding to your `colors.xml`



