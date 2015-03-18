package org.asmatron.messengine.observ;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

@SuppressWarnings({"unchecked", "rawtypes"})
public class TestObservables {

  @Mock
  private Observer<ObserveObject> observer;
  @Spy
  private final Observable<ObserveObject> observable = new Observable<>();
  @Mock
  private Observer<ObservePropertyChanged<TestObservables, String>> propObserv;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testname1() throws Exception {
    observable.add(observer);
    observable.fire(ObserveObject.EMPTY);
    verify(observer).observe(ObserveObject.EMPTY);
  }

  @Test
  public void testname11() throws Exception {
    observable.add(observer);
    ObservValue<String> param = new ObservValue<>("ss");
    observable.fire(param);
    verify(observer).observe(param);
    assertEquals("ss", param.getValue());
  }

  @Test
  public void testname2() throws Exception {
    observable.add(observer);
    observable.remove(observer);
    observable.fire(ObserveObject.EMPTY);
    verify(observer, never()).observe(ObserveObject.EMPTY);

  }

  @Test
  public void testname3() throws Exception {
    observable.add(observer);
    observable.clean();
    observable.fire(ObserveObject.EMPTY);
    verify(observer, never()).observe(ObserveObject.EMPTY);
  }

  @Test
  public void testProperties() throws Exception {
    ObservedProperty<TestObservables, String> property = new ObservedProperty<>(this);
    property.on().add(propObserv);
    property.setValue("s");
    assertEquals("s", property.getValue());
    ArgumentCaptor<ObservePropertyChanged> captor = ArgumentCaptor.forClass(ObservePropertyChanged.class);
    verify(propObserv).observe(captor.capture());
    ObservePropertyChanged value = captor.getValue();
    assertEquals("s", value.getValue());
    assertNull(value.getOldValue());
    assertEquals(this, value.getSource());
    property.setValue("s");
    verifyNoMoreInteractions(propObserv);
  }

}
