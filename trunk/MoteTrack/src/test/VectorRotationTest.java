/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import data.Vector3d;

/**
 *
 * @author Scheinecker Thomas
 */
public class VectorRotationTest {
    public static void main(String[] args) {
        Vector3d v = new Vector3d(Vector3d.xAxis);
        System.out.println(v);
        v.rotate(90, Vector3d.zAxis);
        System.out.println(v);
    }
}
