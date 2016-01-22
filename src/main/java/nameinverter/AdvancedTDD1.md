# Advanced TDD 1

[원문](http://cleancoders.com/episode/clean-code-episode-19-p1/show)

### TDD Review - The Three Laws

- 1st law: before you write any production code you must write a test code for that code and of course that test fail
- 2nd law: stop writing test as soon as it fails even if it is compile error.
	- 테스트가 컴파일 에러를 포함해서 실패하면 바로 그만두고 production code를 작성한다.
- 3rd law: stop writing production code as soon as the test passes

이 3가지 규칙을 지키며 싸이클을 돈다. 아마 30초 단위의 작업이 될 수도 있다.

한번에 여려가지 것에 집중하는 것은 어렵다.

- 처음에는 문제를 정의하는 것에만 집중한다. failing test를 작성함으로써 - Red Phase
- 그 다음에는 그 문제를 해결하는 것에만 집중한다. failing test를 pass하도록 함으로써 - Green Phase
- 그 다음에는 지금껏 만든 mass를 clean up하는 것에 집중한다. 리팩토링을 수행함으로써 - Blue Phase

## Name Inverter

위에 정의한 규칙을 지키는 예제를 살펴본다.

이 예제는 `First Last`로 입력이 들어오면 `Last, First`를 반환한다.

### 0. 실행 환경 구축

항상 아래와 같은 XXXTest.java를 만드는 것으로 시작

```
@Test
public void foo() {
}
```

필요한 라이브러리 설정 등을 통해 동작하는지 확인하기 위해 실행시켜 본다. 

항상 실행시킬 수 있는 뭔가를 가지고 있는 것은 중요하다.

### 1. given_null___returns_empty_string

test는 가장 흥미있지만 가장 단순한(Null, Empty String 등) 순서로 추가한다.

NameInverter에서 가장 단순한 테스트는 null을 전달하는 것이다.

##### 1.1 add failing test

```
public class NamerInverterTest {
	@Test
	public void given_null___returns_empty_string() {
		assertThat(invert(null), is(""));
	}
}
```

##### 1.2 make it passes

![](https://api.monosnap.com/rpc/file/download?id=cYY6oZCD3tMYpfCLMw0snnxleB058A)


[github](https://github.com/msbaek/code-samples/commit/5b61163047e1d14e959b2ac93915bbf71e208e8b)

##### 1.3 refactoring

N/A

### 2. given_empty_string___returns_empty_string

##### 2.1 add failing test

```
@Test
public void given_empty_string___returns_empty_string() {
	assertThat(invert(""), is(""));
}
```

##### 2.2 make it passes

N/A

### 3. given_simple_name___returns_simple_name

##### 3.1 add failing test

```
@Test
public void given_simple_name___returns_simple_name() {
	assertThat(invert("Name"), is("Name"));
}
```

##### 3.2 make it passes

![](https://api.monosnap.com/rpc/file/download?id=wwPGU74dnLr3sILClWD3QjRd8LoGwa)

이상과 같이 간단한 조건을 추가해서 해결 가능

##### 3.3 refactoring

N/A

### 4. given_first_last___returns_last_first

##### 4.1 add failing test

```
@Test
public void given_first_last___returns_last_first() {
	assertThat(invert("First Last"), is("Last, First"));
}
```

##### 4.2 make it passes

![](https://api.monosnap.com/rpc/file/download?id=dzNH5JEWZWjlndl1TZl3UVhbCtGSyd)

##### 4.3 refactoring

![](https://api.monosnap.com/rpc/file/download?id=L7YQW3ZxRlOlXWSE0BIHJ0osU30BDP)

이전 코드는 names.length == 2인 경우가 special case이다. 2인 경우를 제외한 모든 경우(else)가 디폴트이다. 하지만 실제로 이름은 first name, last name 2개를 갖는 것이 디폴트이다.

이를 위해 if 문장을 선택하고 intellij에서 invert condition을 실행한다.

![](https://api.monosnap.com/rpc/file/download?id=PywkTdnJlkFlgQp0f3ufZT5CApoYMM)

names.length != 2를 names.length == 1로 변경하여 이름이 하나의 단어인 경우가 특별한 경우가 되도록 한다.

### 5. given_simple_name_with_leading_spaces___returns_simple_name

##### 5.1 add failing test

```
@Test
public void given_simple_name_with_leading_spaces___returns_simple_name() {
	assertThat(invert(" Name"), is("Name"));
}
```

##### 5.2 make it passes

![](https://api.monosnap.com/rpc/file/download?id=ITSfPHKBSnd0pEHs8Hkh6vCVBRvzF2)

##### 5.3 refactoring

인자로 전달받은 값을 변경하는 것 위험하다.

![](https://api.monosnap.com/rpc/file/download?id=ZQIcDT09FsO9QGT7FH8RAtCKAKN1Wk)

### 6. given_first_last_with_multiple_spaces_between___returns_last_first

##### 6.1 add failing test

```
@Test
public void given_first_last_with_multiple_spaces_between___returns_last_first() {
	assertThat(invert("First   Last"), is("Last, First"));
}
```

##### 6.2 make it passes

![](https://api.monosnap.com/rpc/file/download?id=S5feNdj3OVsn887CilJTzUJUY4y2VO)

##### 6.3 refactoring

if-else symmetry를 맞춘다.

![](https://api.monosnap.com/rpc/file/download?id=nXHyDL93HEs8CbN8hKfflsRz7K9pu6)

### 7. given_honorific_and_first_last___returns_last_first

##### 7.1 add failing test

```
@Test
public void given_honorific_and_first_last___returns_last_first() {
	assertThat(invert("Mr. First Last"), is("Last, First"));
}
```

##### 7.2 make it passes

![](https://api.monosnap.com/rpc/file/download?id=gGeSoCIfOJTMXVzRWVV5IRIxv54phP)

array대신 원소를 삭제하기 편한 list로 변환 후 호칭이 있으면 제거

##### 7.3 refactoring

extract methods

![](https://api.monosnap.com/rpc/file/download?id=ANYYvzGnKaQfKsClvHrebVqAGHWfqy)

##### 7.4 add more failing logical test

```
@Test
public void given_honorific_and_first_last___returns_last_first() {
	assertThat(invert("Mr. First Last"), is("Last, First"));
	assertThat(invert("Mrs. First Last"), is("Last, First"));
}
```

Singe Assert Rule에서 assert는 logic한 것이다. 따라서 `assertThat(invert("Mrs. First Last"), is("Last, First"));`를 별도의 테스트 메소드에서 assert하는 대신 논리적으로 같은 의미를 갖는 메소드에 추가한다.

##### 7.5 make it passes

![](https://api.monosnap.com/rpc/file/download?id=qbna85vgLkx54e7URAnLCPjjRlz09O)

##### 7.6 refactoring

![](https://api.monosnap.com/rpc/file/download?id=z7p3dOsHQFeD23qKvyCcG3zXvmEmXc)

### 8. given_post_nominals_exists___post_nominals_stays_at_end

##### 8.1 add failing test

```
@Test
public void given_post_nominals_exists___post_nominals_stays_at_end() {
	assertThat(invert("First Last Sr."), is("Last, First Sr."));
}
```

##### 8.2 make it passes

![](https://api.monosnap.com/rpc/file/download?id=8aLV0MVpFX472NcKkGteZvxo5cqSDS)

##### 8.3 add more failing logical test

![](https://api.monosnap.com/rpc/file/download?id=cNLvrrDqW6u3cf5zOyltBGZqeobpgp)

##### 8.4 make it passes

![](https://api.monosnap.com/rpc/file/download?id=jRcKWCxCUiwjRL5ae2E1dKEV2H6jbX)

##### 8.5 refactoring

![](https://api.monosnap.com/rpc/file/download?id=LbTXa2mqykz7z0jiAM3HjtYhCD4Ghi)

extract method

### 9. integrated_case

##### 9.1 add failing test

```
@Test
public void integrated_case() {
	assertThat(invert("   Rober Martin III esq.  "), is("Martin, Rober III esq."));
}
```

##### 9.2 make it passes

N/A

##### 9.3 refactoring

![](https://api.monosnap.com/rpc/file/download?id=ERiXDqoIESS1cVSJ0BHKxHU8fyb8Or)
inline variable

![](https://api.monosnap.com/rpc/file/download?id=bTbZFfvhAYD2uWesQymWyofVVKzRe3)
move if to caller from callee

![](https://api.monosnap.com/rpc/file/download?id=yPxhvaoJfRsPKzaKtibSGFHBxPUSj4)
extract method

![](https://api.monosnap.com/rpc/file/download?id=qFApYIawyw7jOaf8wZdf7q1r1UZGfc)
extract method

![](https://api.monosnap.com/rpc/file/download?id=XcgkJjaACPMBYm1TkrP0eEC52vQTi8)
rename

![](https://api.monosnap.com/rpc/file/download?id=4lZOZ8LEKn1KgV0aI7WTSOiy37cY8s)
extract method

![](https://api.monosnap.com/rpc/file/download?id=b2RiXdrQEzKzp54HDUHFIJknPkW0Rq)
refactor till remove {}

![](https://api.monosnap.com/rpc/file/download?id=udXthX7rHIefWLnVpP4xxkyne90Rn7)
extract method object