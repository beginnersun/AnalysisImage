package com.example.base_module.util

class UnionFind {

    private var tree: Array<Int>
    private var degree:Array<Int>
    private var num: Int = 0


    constructor(num: Int) {
        this.num = num
        tree = Array(num) { index ->
            index
        }
        degree = Array(num){
            1
        }
    }

    /**
     * 查找对应的根节点 并压缩路径
     */
    fun find(index: Int):Int {
        if (tree[index] != index) {
            tree[index] = find(tree[index])
        }
        return tree[index]
    }

    /**
     * 将两个集合合并
     */
    fun union(u1:Int, u2:Int){
        val u1Root = find(u1)
        val u2Root = find(u2)
        if (u1Root == u2Root) return
        if (degree[u1Root] > degree[u2Root]){
            tree[u2Root] = u1Root
            degree[u1Root] += degree[u2Root]
        }else{
            tree[u1Root] = u2Root
            degree[u2Root] += degree[u1Root]
        }
    }
}
class SavingUnionFind{
    private var tree: Array<Int>
    private var num: Int = 0


    constructor(num: Int) {
        this.num = num
        tree = Array(num) {
            -1
        }
    }

    /**
     * 查找对应的根节点 并压缩路径
     * <0说明是根节点
     */
    fun find(index: Int):Int {
        if (tree[index] < 0){
            return index
        }
        tree[index] = find(tree[index])
        return tree[index]
    }

    /**
     * 将两个集合合并
     */
    fun union(u1:Int, u2:Int){
        val u1Root = find(u1)
        val u2Root = find(u2)
        if (u1Root == u2Root) return
        if (tree[u1Root] > tree[u2Root]){  //注意此时根节点大小为负值 所以 大于代表深度小
            tree[u1Root] = u2Root
        }else if (tree[u1Root] == tree[u2Root]){
            tree[u1Root] = u2Root
            tree[u2Root]--  //深度加一
        }else{
            tree[u2Root] = u1Root
            tree[u1Root]--
        }
    }
}