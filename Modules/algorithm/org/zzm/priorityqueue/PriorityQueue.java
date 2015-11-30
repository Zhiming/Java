package org.zzm.priorityqueue;

//父节点比两个子节点都大的heap
public class PriorityQueue {
	
	private int[] array;
	
	public PriorityQueue(int[] a){
		array = new int[a.length + 1];
	}

	public int size() {
		return this.array.length - 1;
	}
	
	public void swim(int k){
		while(k > 1 && array[k] > array[k/2]){
			int temp = array[k/2];
			array[k] = temp;
			array[k] = array[k/2];
			array[k/2] = temp;
			k = k / 2;
		}
	}
	
	public void sink(int k){
		//判断k是否是叶节点
		while(2 * k < this.array.length){
			int j = 2 * k;
			// N = array.length - 1，注意第一个位置是不用的
			//判断右子树是否存在，如果j是最后一个元素的话，那么j会刚好等于N，所以如果j<N的话，那么就有右元素存在
			if(j < this.array.length - 1){
				//确认左右子节点哪个大
				if(this.array[j] < this.array[j + 1]){
					j++;
				}
			}
			
			//判断父节点是否比较大的子节点大
			if(array[k] > array[j]) break;
			
			//交换父元素和右子元素
			int temp = array[k];
			array[k] = array[j];
			array[j] = temp;
		}
	}
	
}
