# ThreeTaps

**2023 Summer Madcamp Week 1 Project**

Team member: [Ohyeon Kwon](https://github.com/fbre0717), [Ihchae Ryu](https://github.com/ihchaeryu)

Development period : 2023-06-29 ~ 2023-07-05

---

<br>

- [ThreeTaps](#threetaps)
  - [개발 환경](#개발-환경)
  - [Tab 1 : Contacts](#tab-1--contacts)
    - [Major features](#major-features)
    - [Technology](#technology)
  - [Tab 2 : Photos](#tab-2--photos)
    - [Major features](#major-features-1)
    - [Technology](#technology-1)
  - [Tab 3 : Music](#tab-3--music)
    - [Major features](#major-features-2)
    - [Technology](#technology-2)

<br>

---

## 개발 환경

- OS: Android
- Language: Java
- IDE: Android Studio Flamingo

<br>

## Tab 1 : Contacts

### Major features

![Tab1_screenshots](imgs/)

- 기기에 저장된 연락처를 가져옵니다.
- 이름에 특정 텍스트가 포함된 연락처를 검색할 수 있습니다.
- 특정 연락처를 터치하여 상세 정보를 확인할 수 있습니다. 
  - 상세 페이지에서 아이콘을 터치하여 해당 연락처에게 즉시 전화, 메시지를 실행할 수 있습니다.

### Technology

- Runtime Permissions
  
  default 화면인 첫 번째 탭에서 앱을 실행하는 데 필요한 모든 권한을 요청합니다. 사용한 권한들은 [AndroidManifest](app/src/main/AndroidManifest.xml) 에서 확인할 수 있습니다.

  Runtime 중에 권한을 요청하기 위해 [Dexter](https://github.com/Karumi/Dexter) library를 사용합니다.

- RecyclerView
  
  RecyclerView 와 해당 RV의 Adapter, RV의 각 element가 되는 ViewHolder, RV의 layout manager 을 설정합니다. 

  override 한 함수인 onCreateViewHolder, onBindViewHolder 에서 실제 작업을 설정합니다. 세부 작업이 다르지만 세 개의 탭 모두 전체적으로 유사한 플로우의 RV를 사용합니다.

<br>

## Tab 2 : Photos

![Tab2_screenshots](imgs/)

### Major features

- 기기에 저장된 사진을 가져옵니다.
- 두 손가락으로 확대, 축소 제스처를 취하면 한 줄에 보이는 사진의 개수를 조절할 수 있습니다.(2개~10개)
- 사진을 클릭하면 전체화면으로 확대됩니다.
  - 두 손가락으로 확대, 축소, 이동 등을 할 수 있습니다.
  - 기본 화면에서 두 번 연속 누르면 1차 확대, 2차 확대, 전체화면으로 축소 이 3가지가 순환됩니다.
  - Next, Previous 버튼을 누르면 다음, 이전 사진을 볼 수 있습니다.

### Technology
- RecyclerView
  
  추가적으로 갤러리 형태로 보여주기 위해 LinearLayoutManager대신에 GridLayoutManager를 사용합니다.

- 사진 데이터 : MediaStore.Images.Media

  기기의 외부 저장소에서 사진 파일을 가져오기 위해 contentResolver을 이용하고, URI로 저장합니다.

- [glide](https://github.com/bumptech/glide)
  
  사진 로드를 빠르게 하기 위해 `glide` library를 사용합니다.
  
- [PhotoView](https://github.com/Baseflow/PhotoView)

  사진의 확대, 축소 등의 기능을 사용하기 위해 사진을 전체화면으로 보여주는 경우 `ImageView`가 아닌 `PhotoView`를 사용합니다.


<br>

## Tab 3 : Music

![Tab3_screenshots](imgs/)

### Major features

- 기기에 저장된 음악을 가져옵니다.
- 특정 음악을 터치하여 플레이어를 실행시킬 수 있습니다.
- 동시에 음악의 상세 페이지를 확인할 수 있습니다.
  - 버튼을 조작하여 음악을 정지/재생하거나 이전/다음 음악으로 전환할 수 있습니다.
  - SeekBar을 조작하여 원하는 지점부터 음악을 재생할 수 있습니다. 

### Technology

- 음악 데이터 : MediaStore.Audio.Media
  
  기기의 외부 저장소에서 Audio 파일, 특히 Music에 속하는 데이터를 가져오기 위해 contentResolver을 이용하여 MediaStore.Audio.Media 에 접속합니다. 

- 음악 재생 : MediaPlayer
  
  Android MediaPlayer을 이용합니다. 재생하고자 하는 음악의 uri를 인자로 받아 새로운 MediaPlayer instance를 생성합니다. 함수를 통해 instance를 정지하거나 재생하는 등의 조작을 할 수 있습니다. 다른 음악을 재생할 때는 현재 resource를 release 한 뒤에 다시 uri를 받아 instance를 생성합니다.
