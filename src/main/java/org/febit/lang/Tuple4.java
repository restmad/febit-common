// Copyright (c) 2013-present, febit.org. All Rights Reserved.
package org.febit.lang;

import java.util.Objects;

/**
 *
 * @author zqq90
 * @param <T1>
 * @param <T2>
 * @param <T3>
 * @param <T4>
 */
public class Tuple4<T1, T2, T3, T4> {

    public static <T1, T2, T3, T4> Tuple4<T1, T2, T3, T4> create(T1 _1, T2 _2, T3 _3, T4 _4) {
        return new Tuple4<>(_1, _2, _3, _4);
    }

    public final T1 _1;
    public final T2 _2;
    public final T3 _3;
    public final T4 _4;

    protected int _hashCode = 0;

    public Tuple4(T1 _1, T2 _2, T3 _3, T4 _4) {
        this._1 = _1;
        this._2 = _2;
        this._3 = _3;
        this._4 = _4;
    }

    @Override
    public int hashCode() {
        int hash = _hashCode;
        if (hash == 0) {
            hash = 7;
            hash = 97 * hash + Objects.hashCode(this._1);
            hash = 97 * hash + Objects.hashCode(this._2);
            hash = 97 * hash + Objects.hashCode(this._3);
            hash = 97 * hash + Objects.hashCode(this._4);
            _hashCode = hash;
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Tuple4<?, ?, ?, ?> other = (Tuple4<?, ?, ?, ?>) obj;
        if (!Objects.equals(this._1, other._1)) {
            return false;
        }
        if (!Objects.equals(this._2, other._2)) {
            return false;
        }
        if (!Objects.equals(this._3, other._3)) {
            return false;
        }
        if (!Objects.equals(this._4, other._4)) {
            return false;
        }
        return true;
    }

}
