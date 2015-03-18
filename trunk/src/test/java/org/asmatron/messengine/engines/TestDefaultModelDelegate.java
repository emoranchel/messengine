package org.asmatron.messengine.engines;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.asmatron.messengine.model.ModelId;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

public class TestDefaultModelDelegate {

  @InjectMocks
  private final DefaultModelDelegate modelDelegate = new DefaultModelDelegate();
  @Spy
  private final Map model = new HashMap();
  @Mock
  private ModelId<String> modelId;
  @Mock
  private ModelId<List<String>> listModelId;
  @Spy
  private final List<String> returnCollectionValue = new ArrayList<String>();

  private final String returnValue = "value";

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void shouldAssignValuesAndTypes() throws Exception {
    modelDelegate.start();

    modelDelegate.set(modelId, returnValue);

    verify(model).put(modelId, returnValue);
    assertTrue(modelDelegate.get(modelId) instanceof String);
    assertEquals(modelDelegate.get(modelId), returnValue);
  }

  @Test
  public void shouldAssignCollectionValueAndType() throws Exception {
    modelDelegate.start();

    modelDelegate.set(listModelId, returnCollectionValue);

    verify(model).put(listModelId, returnCollectionValue);
    assertTrue(modelDelegate.get(listModelId) instanceof List);
    assertEquals(modelDelegate.get(listModelId), returnCollectionValue);
  }

  @Test(expected = IllegalStateException.class)
  public void shouldThrowIlegalStateExceptionInSetter() throws Exception {
    modelDelegate.stop();

    modelDelegate.set(listModelId, returnCollectionValue);
    verify(model, never()).put(listModelId, returnCollectionValue);
  }

  @Test(expected = IllegalStateException.class)
  public void shouldThrowIlegalStateExceptionInGetter() throws Exception {
    modelDelegate.stop();

    modelDelegate.get(listModelId);
    verify(model, never()).get(listModelId);
  }

}
