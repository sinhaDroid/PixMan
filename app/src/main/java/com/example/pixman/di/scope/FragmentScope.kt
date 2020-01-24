package com.openapp.tusk.di.scope

import javax.inject.Scope

/**
 * Created by deepanshu on 16/11/17.
 * A scoping annotation to permit objects whose lifetime should
 * conform to the life of the Fragment to be memorised in the
 * correct component.
 */

@MustBeDocumented
@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class FragmentScope